from flask import Flask, request, send_file, jsonify
from flask_cors import CORS
import json
import os

from app_utils import initializeSystem, loadData, uploadPath, profile_image_data_path, userDataJsonPath, userProgressJsonPath
from finger_counting import analyzeFingerCount
from sinhala_prediction import isPredictionTrue
from weakness_prediction import predict_weakness
from emotion_analyze import analyzeEmotions

app = Flask(__name__)
CORS(app)

initializeSystem()
# loadData
userData, userProgressData, userEmotionData = loadData()

@app.route('/register', methods=['POST'])
def register():
    try:
        # Get the data from the form
        email = request.form.get('email', type=str)
        name = request.form.get('name', type=str)
        password = request.form.get('password', type=str)

        if 'profile_picture' in request.files:
            profile_picture = request.files['profile_picture']
            profile_picture.save(profile_image_data_path + email + ".jpg")

        error = 0
        errorDesc = "Email already used"

        # Get all ids
        all_ids = userData.keys()

        if email not in all_ids:
            #add userdata with the next index
            userData[email] = {
                'email': email,
                'password': password,
                'name': name,
                'isFirstTimeLoggedIn': "False" 
            }

            with open(userDataJsonPath, 'w') as json_file:
                json.dump(userData, json_file, indent=4)
        else:
            error = 1

        if email not in userProgressData:
            userProgressData[email] = {
                'weakness': "0",
                'marks': {}
            }
            with open(userProgressJsonPath, 'w') as json_file:
                json.dump(userProgressData, json_file, indent=4)

        
        response_data = {
            'error': error,
            'errorDesc': errorDesc
        }

        return response_data, 200

    except Exception as e:
        return {"error_1": str(e)}, 500
    
@app.route('/sign_in', methods=['POST'])
def sign_in():
    try:
        # Get the data from the form
        email = request.form.get('email', type=str)
        password = request.form.get('password', type=str)

        error = 0
        errorDesc = ""
        username = ""

        print(str(userData))

        # Get all ids
        all_ids = userData.keys()
        firstTimeLoggedIn = None

        if email not in all_ids:
            error = 1
            errorDesc = "Email not found"
        elif password != userData.get(email)["password"]:
            error = 1
            errorDesc = "Password not correct"
        else:
            error = 0
            username = userData.get(email)["name"]
            firstTimeLoggedIn = userData.get(email)["isFirstTimeLoggedIn"]

        response_data = {
            'error': error,
            'errorDesc': errorDesc,
            "username": username,
            "isFirstTimeLoggedIn": firstTimeLoggedIn
        }

        print(response_data)

        if (firstTimeLoggedIn == "False"):
            # save isFirstTimeLoggedIn to True
            userData[email]["isFirstTimeLoggedIn"] = "True"
            with open(userDataJsonPath, 'w') as json_file:
                    json.dump(userData, json_file, indent=4)

        return response_data, 200

    except Exception as e:
        return {"error_1": str(e)}, 500
    
@app.route('/get_pfp', methods=['POST'])
def get_pfp():
    try:
        # Get the data from the form
        email = request.form.get('email', type=str)

        profile_picture_path = os.path.join(profile_image_data_path, f"{email}.jpg")

        if os.path.exists(profile_picture_path):
            return send_file(profile_picture_path, mimetype='image/jpeg')
        else:
            return {'error': "not found"}

    except Exception as e:
        return {"error_1": str(e)}, 500
    
@app.route('/set_pfp', methods=['POST'])
def set_pfp():
    try:
        # Get the data from the form
        email = request.form.get('email', type=str)

        if 'profile_picture' in request.files:
            profile_picture = request.files['profile_picture']
            profile_picture.save(profile_image_data_path + email + ".jpg")

        return "OK"

    except Exception as e:
        return {"error_1": str(e)}, 500
    
@app.route('/set_username', methods=['POST'])
def set_username():
    try:
        # Get the data from the form
        email = request.form.get('email', type=str)
        name = request.form.get('name', type=str)

        error = 0
        errorDesc = ""

        # Get all ids
        all_ids = userData.keys()

        if email not in all_ids:
            errorDesc = "Email not found"
            error = 1
        else:
            userData.get(email)["name"] = name
        
        response_data = {
            'error': error,
            'errorDesc': errorDesc
        }
        return response_data

    except Exception as e:
        return {"error_1": str(e)}, 500
    
@app.route('/get_dashboard_data', methods=['POST'])
def get_dashboard_data():
    # try:
    # Get the data from the form
    email = request.form.get('email', type=str)

    # response data containers
    error = 0
    errorDesc = ""
    emotionData = []
    weakSubject = ""
    weakSubjectOldMarks = ""
    weakSubjectNewMarks = ""
    marks = {}

    # Get emotion data
    all_emotion_ids = userEmotionData.keys()
    if email in all_emotion_ids:
        emotionData = userEmotionData[email]
    else:
        error = 1
        errorDesc = "No emotion data found"

    # Get weak subject
    all_progress_ids = userProgressData.keys()
    if email in all_progress_ids:
        if "weekness" in userProgressData[email]:
            weakness = userProgressData.get(email)["weekness"]
            weakSubject = "isxy," if weakness == "1" else ".‚;h"  
            if "marks" in userProgressData[email]:
                marks = userProgressData.get(email)["marks"]
                if weakness == "0": 
                    if "mathMarks" in userProgressData[email]["marks"]:
                        weakSubjectOldMarks = userProgressData.get(email)["marks"]["mathMarks"]
                    if "mathQuiz" in userProgressData[email]["marks"]:
                        weakSubjectNewMarks = userProgressData.get(email)["marks"]["mathQuiz"]
                else:
                    if "sinhalaMarks" in userProgressData[email]["marks"]:
                        weakSubjectOldMarks = userProgressData.get(email)["marks"]["sinhalaMarks"]
                    if "sinhalaQuiz" in userProgressData[email]["marks"]:
                        weakSubjectNewMarks = userProgressData.get(email)["marks"]["sinhalaQuiz"]

            
    else:
        error = 2
        errorDesc = "No weak subject found"
        
    response_data = {
        'error': error,
        'errorDesc': errorDesc,
        'emotionData': emotionData,
        'weakSubject': weakSubject,
        'weakSubjectOldMarks': weakSubjectOldMarks,
        'weakSubjectNewMarks': weakSubjectNewMarks,
        'marks': marks
    }

    return response_data, 200

    # except Exception as e:
    #     return {"error_1": str(e)}, 500

@app.route('/save_marks', methods=['POST'])
def save_marks():
    # try:
    # Get the data from the form
    email = request.form.get('email', type=str)
    marks = request.form.get('marks', type=str)
    activityName = request.form.get('activityName', type=str)

    # response data containers
    error = 0
    errorDesc = ""
    
    # save marks
    currentMarks = userProgressData.get(email)['marks']
    currentMarks[activityName] = marks
    with open(userProgressJsonPath, 'w') as json_file:
        json.dump(userProgressData, json_file, indent=4)
        
    response_data = {
        'error': error,
        'errorDesc': errorDesc
    }

    return response_data, 200

    # except Exception as e:
    #     return {"error_1": str(e)}, 500










@app.route('/predict_sinhala', methods=["POST"])
def predictSinhala():
    # Get the image data from the request
    image_data = request.files['image']
    # Get the selected letter from the request
    selected_letter = request.form['selectedLetter']
    # save the image
    image_data.save(uploadPath + '/sinhala.png')
    return isPredictionTrue(selected_letter)

@app.route('/analyze_finger_count', methods=['POST'])
def analyze_finger_count():
    # Access Image File
    video_data = request.files['video']

    # Get the selected sentence from the request
    selected_gestures = request.form['mainParam']

    video_path = uploadPath + 'finger_count.mp4'

    #save the image
    video_data.save(video_path)

    # analyzeGesture()
    response = analyzeFingerCount(video_path, selected_gestures)

    # Delete the file
    os.remove(video_path)

    return response

@app.route('/analyze_emotions', methods=['POST'])
def analyze_emotions():
    # Access Image File
    email = request.form['email']
    video_data = request.files['video']

    video_path = uploadPath + 'emotion_analyze.mp4'

    #save the image
    video_data.save(video_path)

    response = analyzeEmotions(video_path, email, userEmotionData)

    # Delete the file
    os.remove(video_path)

    return response

@app.route('/get_weakness', methods=['POST'])
def get_weakness():
    # Get the data from the request
    email = request.form['email']
    mathsIncorrectRatio = request.form['mathsIncorrectRatio']
    sinhalaIncorrectRatio = request.form['sinhalaIncorrectRatio']
    mathsMarks = request.form['mathsMarks']
    sinhalaMarks = request.form['sinhalaMarks']

    # get the prediction
    response = predict_weakness(float(sinhalaMarks), float(mathsMarks), float(sinhalaIncorrectRatio), float(mathsIncorrectRatio), email, userProgressData)

    return response




if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')