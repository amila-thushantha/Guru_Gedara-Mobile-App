import os
import json

def createFolder(folderPath):
    if not os.path.exists(folderPath):
        os.makedirs(folderPath)

def createJSONFile(filePath):
    # Check if the file exists
    if not os.path.exists(filePath):
        # If the file doesn't exist, create it and write some initial data
        with open(filePath, 'w') as file:
            initial_data = {}  # You can put any initial data you want here
            json.dump(initial_data, file, indent=4)

def deleteFile(filePath):
    if os.path.exists(filePath):
        os.remove(filePath)
        print(f"Deleted: {filePath}")
    else:
        print(f"File not found: {filePath}")

uploadPath = "program_data/uploads/"
resizedPath = "program_data/resized/"
json_data_path = 'user_data/json_data/'
profile_image_data_path = 'user_data/profile_images/'

userDataJsonPath = json_data_path + "userdata.json"
userProgressJsonPath = json_data_path + "userProgress.json"
userEmotionDataJsonPath = json_data_path + "userEmotion.json"

# intialize json data variables
# def getUserData():
#     with open(userDataJsonPath, 'r') as file:
#         userData = json.load(file)
#     return userData

# def getUserProgressData():
#     with open(userProgressJsonPath, 'r') as file:
#         userProgressData = json.load(file)
#     return userProgressData

# def getUserEmotionData():
#     with open(userEmotionDataJsonPath, 'r') as file:
#         userEmotionData = json.load(file)
#     return userEmotionData

def loadData():
    def safe_load_json(file_path):
        if os.path.exists(file_path) and os.stat(file_path).st_size > 0:
            try:
                with open(file_path, 'r') as file:
                    return json.load(file)
            except json.JSONDecodeError:
                print(f"Warning: Invalid JSON in {file_path}. Resetting...")
        return {}

    userData = safe_load_json(userDataJsonPath)
    userProgressData = safe_load_json(userProgressJsonPath)
    userEmotionData = safe_load_json(userEmotionDataJsonPath)

    return userData, userProgressData, userEmotionData


def initializeSystem():
    print('System initializing...')

    createFolder(uploadPath)
    createFolder(resizedPath)
    createFolder("program_data/haarcascades")
    createFolder(json_data_path)
    createFolder(profile_image_data_path)

    createJSONFile(userDataJsonPath)
    createJSONFile(userProgressJsonPath)
    createJSONFile(userEmotionDataJsonPath)

    print('System initialized')