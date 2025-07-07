import cv2
import numpy as np
import tensorflow as tf
from keras.models import model_from_json
import mediapipe as mp
from PIL import Image
import matplotlib.pyplot as plt
import threading

model = tf.keras.models.load_model("models/finger_counting_model.h5")

classes = ['finger_0', 'finger_1', 'finger_2', 'finger_3', 'finger_4', 'finger_5']

mp_hands = mp.solutions.hands

error_code = 0

def split_string_to_array(input_string):
    original = input_string.split()
    return original

def crop_hand(image):
    """
    Detect and crop the hand region from the input image.
    Ensure the cropped image is square, resized to medium size,
    and centered on a black canvas.
    """

    # Wait briefly to allow the display
    cv2.waitKey(1)

    image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    with mp_hands.Hands(static_image_mode=True, max_num_hands=1, min_detection_confidence=0.5) as hands:
        # Process the image
        results = hands.process(image_rgb)

        # Check if a hand is detected
        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                # Get bounding box coordinates
                h, w, _ = image.shape
                x_coords = [int(lm.x * w) for lm in hand_landmarks.landmark]
                y_coords = [int(lm.y * h) for lm in hand_landmarks.landmark]
                x_min, x_max = max(0, min(x_coords)), min(w, max(x_coords))
                y_min, y_max = max(0, min(y_coords)), min(h, max(y_coords))

                # Expand the bounding box slightly
                margin = 150  # Adjust margin as needed
                x_min, x_max = max(0, x_min - margin), min(w, x_max + margin)
                y_min, y_max = max(0, y_min - margin), min(h, y_max + margin)

                # Ensure the bounding box is square
                box_width = x_max - x_min
                box_height = y_max - y_min
                square_size = max(box_width, box_height)

                # Adjust coordinates to make it square
                x_center = (x_min + x_max) // 2
                y_center = (y_min + y_max) // 2
                x_min = max(0, x_center - square_size // 2)
                x_max = min(w, x_center + square_size // 2)
                y_min = max(0, y_center - square_size // 2)
                y_max = min(h, y_center + square_size // 2)

                # Crop the hand region
                hand_crop = image[y_min:y_max, x_min:x_max]

                # Convert cropped image to grayscale
                hand_crop_gray = cv2.cvtColor(hand_crop, cv2.COLOR_BGR2GRAY)

                # Resize the cropped image to medium size
                medium_size = 200  # Medium size for the hand region
                hand_resized = cv2.resize(hand_crop_gray, (medium_size, medium_size), interpolation=cv2.INTER_AREA)

                # Create a larger black canvas
                canvas_size = 256  # Size of the black canvas
                canvas = np.zeros((canvas_size, canvas_size), dtype=np.uint8)

                # Center the resized hand on the black canvas
                y_offset = (canvas_size - medium_size) // 2
                x_offset = (canvas_size - medium_size) // 2
                canvas[y_offset:y_offset + medium_size, x_offset:x_offset + medium_size] = hand_resized

                return canvas

    # If no hand is detected, return None
    return None

def predict_finger_count(frame):
    # Crop the hand region
    cropped_hand = crop_hand(frame)

    # Check if a hand was cropped
    if cropped_hand is not None:
        # Convert the cropped hand to grayscale and resize it
        img = Image.fromarray(cv2.cvtColor(cropped_hand, cv2.COLOR_BGR2RGB)).convert("L").resize((224, 224))
    else:
        print("No hand detected in the image.")
        return None

    # Convert grayscale to a NumPy array for OpenCV processing
    img_array = np.array(img)

    # Convert binary mask back to RGB by stacking channels
    binary_rgb = np.stack([img_array] * 3, axis=-1)

    # Resize and preprocess the image for MobileNetV2
    img_preprocessed = tf.keras.applications.mobilenet_v2.preprocess_input(binary_rgb)
    img_preprocessed = np.expand_dims(img_preprocessed, axis=0)

    # Get predictions
    predictions = model.predict(img_preprocessed)

    # Get the class with the highest score
    predicted_class = np.argmax(predictions, axis=1)[0]
    predicted_confidence = predictions[0][predicted_class]

    predicted_class_ = classes[predicted_class]
    print(predicted_class_)

    return predicted_class_

def analyzeFingerCount(video_path, selected_gestures_str):
    gestures_predicted = []
    gesture_response = []

    selected_gestures = split_string_to_array(selected_gestures_str)
    print(selected_gestures)

    error_code = 0
    cap = cv2.VideoCapture(video_path)

    if not cap.isOpened():
        print("Error opening video file")
        return

    fps = int(cap.get(cv2.CAP_PROP_FPS))
    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    skip_frames = int(fps * 2) # 2 seconds interval

    # Determine the midpoint of the video
    midpoint = total_frames // 2

    def analyze_gesture_half(start_frame, end_frame, result_list):
        cap = cv2.VideoCapture(video_path)
        cap.set(cv2.CAP_PROP_POS_FRAMES, start_frame)
        while True:
            frame_pos = int(cap.get(cv2.CAP_PROP_POS_FRAMES))
            if frame_pos >= end_frame:
                break
            ret, frame = cap.read()
            if not ret:
                break
            predicted_gesture = predict_finger_count(frame)
            if predicted_gesture is not None:
                result_list.append(predicted_gesture)
            cap.set(cv2.CAP_PROP_POS_FRAMES, frame_pos + skip_frames)
        cap.release()

    # Create threads for gesture analysis on each half
    gestures_first_half = []
    gestures_second_half = []

    first_half_thread = threading.Thread(target=analyze_gesture_half, args=(0, midpoint, gestures_first_half))
    second_half_thread = threading.Thread(target=analyze_gesture_half, args=(midpoint, total_frames, gestures_second_half))

    # Start threads
    first_half_thread.start()
    second_half_thread.start()

    # Wait for both threads to complete
    first_half_thread.join()
    second_half_thread.join()

    # Combine results from both halves
    gestures_predicted = gestures_first_half + gestures_second_half

    if gestures_predicted:
        error_code = 0
        matches = set(selected_gestures) & set(gestures_predicted)
        correct_count = 0
        gesture_response = [gesture in matches for gesture in selected_gestures]
        correct_count = len([gesture for gesture in selected_gestures if gesture in matches])
        score = (correct_count / len(selected_gestures)) * 100
        score = round(score, 1)

        if matches:
            print("Matches found:", matches)
            print(gesture_response)
        else:
            print("No matches found")
    else:
        score = 0
        error_code = 1
        gestures_predicted = []

    responseData = {
        'gestureData': gesture_response,
        'selectedGestures': selected_gestures,
        "score": score,
        "error": error_code,
        "errorDesc": "No hands detected" if error_code == 1 else ""
    }
    print(responseData)
    return responseData
