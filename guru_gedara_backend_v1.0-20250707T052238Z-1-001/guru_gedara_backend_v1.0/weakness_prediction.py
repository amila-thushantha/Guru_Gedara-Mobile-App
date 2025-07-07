import joblib
import numpy as np
import json

from app_utils import userProgressJsonPath

# userProgressData = getUserProgressData()

# Load the trained model and scaler
model = joblib.load("models/weakness_predictor_model.pkl")
scaler = joblib.load("models/scaler.pkl")

# Function to preprocess input and make a prediction
def predict_weakness(sinhala_exam, math_exam, sinhala_incorrect_ratio, math_incorrect_ratio, email, userProgressData):
    error = 0
    prediction_label = ""
    errorDesc = ""
    if (not sinhala_exam or not math_exam or not sinhala_incorrect_ratio or not math_incorrect_ratio):
        error = 1
        errorDesc = "Some values are null"
    else:
        # Prepare the input data
        input_data = np.array([[sinhala_exam, math_exam, sinhala_incorrect_ratio, math_incorrect_ratio]])    
        # Scale the input data
        input_data_scaled = scaler.transform(input_data)  
        # Get the prediction
        prediction = model.predict(input_data_scaled)  
        # Map the prediction to the target class
        # 1 - Sinhala, 0 - Maths
        prediction_label = "isxy," if prediction[0] == 1 else ".‚;h"  

        # save prediction
        userProgressData.get(email)['weekness'] = str(prediction[0])
    
        # save marks    
        currentMarks = userProgressData.get(email)["marks"]
        currentMarks['mathMarks'] = str((1 - math_incorrect_ratio) * 100)
        currentMarks['sinhalaMarks'] = str((1 - sinhala_incorrect_ratio) * 100)
        with open(userProgressJsonPath, 'w') as json_file:
            json.dump(userProgressData, json_file, indent=4)

        print(sinhala_incorrect_ratio)
        print(math_incorrect_ratio)
        print(prediction_label)

    response_data = {
        'error': error,
        'errorDesc': errorDesc,
        'prediction': prediction_label
    }
    return response_data