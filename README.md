# 📚 Guru Gedara – AI-Powered Mobile Learning App for Children with Dyscalculia & Dyslexia

**Guru Gedara** is an innovative mobile learning application developed as a final year research project to assist children with **Dyscalculia** and **Dyslexia** through interactive educational tools and intelligent assistance powered by machine learning.

---

## 🎯 Project Objective

To create a mobile-based intelligent learning environment that adapts to the needs of children with learning disabilities by offering:
- Personalized math and language learning support
- Finger-count based number recognition
- Sinhala handwritten letter recognition
- Real-time emotional tracking via face detection
- Adaptive content suggestions based on subject weakness

---

## 🔧 Technologies Used

### ⚙️ Backend:
- **Flask** – REST API framework
- **Python** – ML model integration and backend logic
- **TensorFlow** – ML model training and inference
- **OpenCV** – Visual processing (image/frame handling)
- **NumPy, Pandas, Scikit-learn** – Data handling and ML
- **JSON** – Backend data structure
- **Jupyter Notebook / Google Colab** – Model training

### 📱 Frontend:
- **Android Studio** – Android application development
- **Java** – Primary programming language
- **SQLite** – Local device storage
- **XML** – UI Design

---

## 🧠 Machine Learning Models

### 1. 🎭 Face Emotion Detection
- **Algorithm:** CNN  
- **Dataset:** [FER-2013](https://www.kaggle.com/datasets/msambare/fer2013)  
- **Purpose:** Detects children's emotions during learning to adjust lesson difficulty and engagement

---

### 2. ✋ Finger Count Recognition
- **Algorithm:** CNN  
- **Dataset:** [Finger Images Dataset](https://www.kaggle.com/datasets/koryakinp/fingers)  
- **Purpose:** Enables number recognition and basic math through finger counting visuals

---

### 3. 📝 Sinhala Handwritten Letter Recognition
- **Algorithm:** CNN  
- **Dataset:** [Sinhala Handwritten Letters](https://www.kaggle.com/datasets/sathiralamal/sinhala-letter-454)  
- **Purpose:** Helps children write and recognize Sinhala letters using camera input or drawing pad

---

### 4. 📉 Subject Weakness Prediction
- **Algorithm:** Logistic Regression  
- **Dataset:** Collected dataset from student quiz/game results  
- **Purpose:** Predicts subject areas where the child is struggling and provides customized content

---

## 👨‍💻 My Contribution – *Amila Thushantha*

- 🔹 Developed and maintained the **Flask backend** for model integration
- 🔹 Integrated ML models for:
  - Finger Count
  - Face Emotion
  - Sinhala Handwriting
  - Subject Weakness Prediction
- 🔹 Built and tested RESTful APIs for mobile-backend communication
- 🔹 Trained CNN-based models in **Google Colab**
- 🔹 Developed key frontend screens using **Android Studio**:
  - Lesson pages
  - Game module
  - Camera integration for prediction
- 🔹 Created data flow using **SQLite** and **JSON**
- 🔹 Managed all backend logic, UI-UX testing, and debugging

---

## 📲 App Features

- 🧮 Math Lessons with visual finger counting
- 🔤 Sinhala Letter Writing Practice
- 😊 Emotion-based feedback and lesson adjustments
- 🎮 Mini educational games with score tracking
- 🧑‍🏫 Personalized suggestions based on performance
- 📈 Offline support with local database (SQLite)

---

## 🚀 How to Run (Optional)

### Backend (Flask):
```bash
pip install -r requirements.txt
python app.py
