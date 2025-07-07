from PIL import Image
import pickle
import tensorflow as tf
import cv2
import numpy as np

from app_utils import deleteFile, uploadPath, resizedPath

# load sinhala model
model_sinhala = tf.keras.models.load_model('models/sinhala_handwritten_cnn.h5')
# load the LB object
loaded_LB = pickle.load(open('models/LB.sav', 'rb'))

uploadImgPath = uploadPath + '/sinhala.png'
resizedImgPath = resizedPath + '/sinhala.png'

def isPredictionTrue(selected_letter):
    img = Image.open(uploadImgPath)

    # Remove alpha channel if it exists
    if img.mode in ('RGBA', 'LA') or (img.mode == 'P' and 'transparency' in img.info):
        img = img.convert('RGB')

    # Resize the image with anti-aliasing to minimize quality loss
    resized_image = img.resize((32, 32), Image.LANCZOS)

    # Save the resized and padded image
    resized_image.save(resizedImgPath)
    
    # Read the image in grayscale
    img = cv2.imread(resizedImgPath, cv2.IMREAD_GRAYSCALE)
    
    # Invert the image: switch black to white and white to black
    img = cv2.bitwise_not(img)
    
    # Convert the image to a numpy array and scale it
    img_array = np.array(img) / 255.0

    # Add a dimension to transform array into a "batch"
    # of size (1, 32, 32, 1)
    img_batch = np.expand_dims(img_array, axis=0)
    img_batch = np.expand_dims(img_batch, axis=-1)
    
    # Get predictions
    predictions = model_sinhala.predict(img_batch)

    # Get the indices of the top 40 probabilities
    top40_indices = np.argpartition(predictions[0], -40)[-40:]

    # Sort the indices by their corresponding probabilities
    top40_indices = top40_indices[np.argsort(predictions[0][top40_indices])][::-1]
    
    # Get the corresponding class names
    top40_classes = loaded_LB.classes_[top40_indices]
    
    # Print the top 40 predictions along with their probabilities
    for i in range(40):
        print(f'Class: {top40_classes[i]}, Probability: {predictions[0][top40_indices[i]]}')

    print(selected_letter)
    returnVal = str(-1)

    for i in range(40):
        if str(top40_classes[i]) == selected_letter:
            returnVal = selected_letter 
            break

    deleteFile(uploadImgPath)
    deleteFile(resizedImgPath)
    
    return returnVal
