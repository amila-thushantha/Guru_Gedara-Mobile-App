import cv2
import numpy as np
from keras.models import model_from_json
from app_utils import userEmotionDataJsonPath
import json

# Load Face detection model
json_file = open('models/emotion_model.json', 'r')
loaded_model_json = json_file.read()
json_file.close()
face_analyze_model = model_from_json(loaded_model_json)
face_analyze_model.load_weights("models/emotion_model.h5")

face_detector = cv2.CascadeClassifier('program_data/haarcascades/haarcascade_frontalface_default.xml')

# emotion_dict = {0: "Angry", 1: "Disgusted", 2: "Fearful", 3: "Happy", 4: "Neutral", 5: "Sad", 6: "Surprised"}
emotion_dict = {0: "තරහ", 1: "පිළිකුල", 2: "බියජනක", 3: "සතුට", 4: "උදාසීන", 5: "දුක", 6: "පුදුම සහගත"}

error_code = 0

def saveTodb(emotionsData, email, userEmotionData):
    #add userdata with the next index
    userEmotionData[email] = emotionsData
    with open(userEmotionDataJsonPath, 'w') as json_file:
        json.dump(userEmotionData, json_file, indent=4)

def checkIsStressed(emotion):
    positive_emotions = ['Happy', 'Surprised', 'Neutral', 'happy', 'calm', 'surprise', 'neutral']
    negative_emotions = ['Angry', 'Disgusted', 'Fearful', 'Sad', 'angry', 'disgust', 'fear', 'sad']

    if emotion in positive_emotions:
        return 'Not Stressed'
    
    if emotion in negative_emotions:
        return 'Stressed'

def createEmoArray(emotions, counts, total_count):
    emoArray = []
    for index in range(len(emotions)):
        percentage = round((int(counts[index]) / total_count) * 100, 2)
        emoArray.append([emotions[index], percentage])
    return emoArray

def split_string_to_array(input_string):
    original = input_string.split()
    return original

def correct_rotation(frame, angle):
    # Get the image center
    (h, w) = frame.shape[:2]
    center = (w // 2, h // 2)
    # Define the rotation matrix
    rotation_matrix = cv2.getRotationMatrix2D(center, angle, 1.0)
    # Perform rotation
    rotated_frame = cv2.warpAffine(frame, rotation_matrix, (w, h))
    return rotated_frame


def analyzeEmotions(video_path, email, userEmotionData):
    emotions_detected = []

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

    # Process face analysis (unchanged from before)
    def face_analysis(video_path, emotions_detected, skip_frames):
        print("Analyzing faces...")
        cap = cv2.VideoCapture(video_path)

        if not cap.isOpened():
            print("Error opening video file")
            return

        while True:
            frame_pos = int(cap.get(cv2.CAP_PROP_POS_FRAMES))
            if frame_pos >= total_frames:
                break
            
            ret, frame = cap.read()
            if not ret:
                print(f"Frame {frame_pos} could not be read.")
                break

            frame = correct_rotation(frame, 0)

            gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            num_faces = face_detector.detectMultiScale(gray_frame, scaleFactor=1.3, minNeighbors=5)

            print(f"Frame {frame_pos} has {len(num_faces)} faces.")

            for (x, y, w, h) in num_faces:
                cv2.rectangle(frame, (x, y-50), (x+w, y+h+10), (0, 255, 0), 4)
                roi_gray_frame = gray_frame[y:y + h, x:x + w]
                cropped_img = np.expand_dims(np.expand_dims(cv2.resize(roi_gray_frame, (48, 48)), -1), 0)

                emotion_prediction = face_analyze_model.predict(cropped_img)
                maxindex = int(np.argmax(emotion_prediction))
                emotion = emotion_dict[maxindex]
                emotions_detected.append(emotion)
                print('From Face: ' + emotion)

            cap.set(cv2.CAP_PROP_POS_FRAMES, frame_pos + skip_frames)

        cap.release()


    face_analysis(video_path, emotions_detected, skip_frames)

    # Process results
    if emotions_detected:
        stressed_count = 0
        total_count = 0
        unique_emotions, counts = np.unique(emotions_detected, return_counts=True)
        all_emotions_with_counts = createEmoArray(unique_emotions, counts, len(emotions_detected))
        print(np.asarray((unique_emotions, counts)).T)

        for index in range(len(unique_emotions)):
            emotion = unique_emotions[index]
            isStressed = checkIsStressed(emotion)
            count = counts[index]
            if isStressed == "Stressed":
                stressed_count += count
            total_count += count

        stressed_percentage = round((stressed_count / total_count) * 100, 2)
        not_stressed_percentage = 100 - stressed_percentage
        if stressed_percentage >= 50:
            emotionsData = ["Stressed", stressed_percentage, all_emotions_with_counts, int(total_count)]
        else:
            emotionsData = ["Not Stressed", not_stressed_percentage, all_emotions_with_counts, int(total_count)]
        print("face stressed percentage: " + str(stressed_percentage) + "%")
    else:
        emotionsData = []
        error_code = 1

    responseData = {
        'emotionsData': emotionsData,
        "error": error_code,
        "errorDesc": "No Face detected" if error_code == 1 else ""
    }

    saveTodb(emotionsData, email, userEmotionData)
    print(responseData)
    return responseData
