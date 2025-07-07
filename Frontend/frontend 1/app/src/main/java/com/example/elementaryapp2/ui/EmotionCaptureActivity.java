package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.TestQuestion;
import com.example.elementaryapp2.classes.face_activity_classes.QuizActivityQuestion;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.CameraFragment.CameraFragment;
import com.example.elementaryapp2.services.CameraFragment.CameraResponseListener;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.quiz.TestQuestionData;
import com.example.elementaryapp2.services.recycler_view.AnswerAdapter;
import com.example.elementaryapp2.services.recycler_view.ChoicesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class EmotionCaptureActivity extends AppCompatActivity implements CameraResponseListener {

    private static final String ARG_PARAM_TYPE = "ARG_PARAM_TYPE";
    private static final String ARG_PARAM_SCREEN_NAME = "ARG_PARAM_SCREEN_NAME";
    private int INTERVAL = 10000; // 10 seconds in mili-seconds
    private int TOTAL_TIME_FOR_Q = 10; // 10 seconds

    private String type;
    private String screenName;
    private int allQuestionCount = 5;
    private int currentQuestionIndex = 0;
    private int secondsRemaining = TOTAL_TIME_FOR_Q;
    private int correctAnswerCount = 0;
    private Handler handler;
    private Runnable questionRunnable;
    private Runnable timeRunnable;

    int totalTimeOut = INTERVAL;

    TextView questionText, totalQuestionCountText, currentQuestionNoText, secondsRemainingView;
    Button startBtn;
    ConstraintLayout quizSection;
    RecyclerView answersRecyclerView;
    CardView answersRecyclerViewSection;

    List<QuizActivityQuestion> quiz = null;

    ImageView imageView;

    private CameraFragment cameraFragment;
    DragFragment dragFragment;

    @Override
    public void onCameraResponseReceived(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        String error = json.getString("error");
        String errorDesc = json.getString("errorDesc");

        JSONArray emotionInfo = new JSONArray();

        if (error.equals("0")) {
            JSONArray EmotionDataArray = json.getJSONArray("emotionsData");
            emotionInfo = EmotionDataArray.getJSONArray(2);
            Log.d("emotionInfo", String.valueOf(emotionInfo));
        }
        AlertDialogUtil.ShowEmotionResultAlertDialog(EmotionCaptureActivity.this, calculateMarks(), emotionInfo, error, R.color.bgClr_1_dark, isClicked -> {
            if (isClicked) {
                Intent intent = new Intent(EmotionCaptureActivity.this, MenuMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onCameraRecordStarted(Boolean response) {
        handleResponseVideoCapture(response);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_capture);

        Services.onPressBack(this);

        quiz = (List<QuizActivityQuestion>) getIntent().getSerializableExtra("quiz");

        handler = new Handler();

        answersRecyclerView = findViewById(R.id.answersRecyclerView);
        answersRecyclerViewSection = findViewById(R.id.answersBg);
        questionText = findViewById(R.id.questionText);
        totalQuestionCountText = findViewById(R.id.totalQuestionCount);
        currentQuestionNoText = findViewById(R.id.currentQuestion);
        secondsRemainingView = findViewById(R.id.secondsView);
        quizSection = findViewById(R.id.quizSection);
        quizSection.setVisibility(View.GONE);

        imageView = findViewById(R.id.questionImage);

        startBtn = findViewById(R.id.buttonStart);
        startBtn.setVisibility(View.VISIBLE);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setVisibility(View.GONE);
                quizSection.setVisibility(View.VISIBLE);
                sendStartRecordingSignal();
            }
        });

        allQuestionCount = quiz.size();
        int totalTime = 0;
        for (QuizActivityQuestion question : quiz) {
            totalTime += question.time;
        }
        totalTimeOut = totalTime * 1000;

        retrieveArguments();
        setupCameraFragment();
    }

    private void loadDragFragment(QuizActivityQuestion question) {
        dragFragment = DragFragment.newInstance(question);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drag_fragment_container, dragFragment);
        transaction.commit();
    }

    private void removeDragFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(dragFragment);
        transaction.commitAllowingStateLoss();
    }

    private void sendStartRecordingSignal() {
        if (cameraFragment != null) {
            cameraFragment.startRecording();
        } else {
            Toast.makeText(this, "Camera fragment not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveArguments() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra(ARG_PARAM_TYPE);
            screenName = getIntent().getStringExtra(ARG_PARAM_SCREEN_NAME);
        }
    }

    private void setupCameraFragment() {
        cameraFragment = CameraFragment.newInstance(type, screenName, "", totalTimeOut);
        addCameraFragment();
    }

    private void handleResponseVideoCapture(Boolean response) {
        if (response) {
            startQuestionRunnable();
        } else {
            stopQuestionRunnable();
        }
    }

    private void updateQuestion() {
        if (quiz != null) {
            QuizActivityQuestion currentQuestion = quiz.get(currentQuestionIndex);
            questionText.setText(currentQuestion.question);

            currentQuestionNoText.setText(String.valueOf(currentQuestionIndex + 1));
            totalQuestionCountText.setText(String.valueOf(quiz.size()));

            TOTAL_TIME_FOR_Q = currentQuestion.time;
            INTERVAL = TOTAL_TIME_FOR_Q * 1000;

            imageView.setImageResource(currentQuestion.imageResource);

            if (currentQuestion.optionsType.equals("text")) {
                // Set the adapter
                ChoicesAdapter adapter = new ChoicesAdapter(currentQuestion.options, this::onAnswerClicked);
                answersRecyclerView.setAdapter(adapter);

                // Set up GridLayoutManager with 2 columns
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                answersRecyclerView.setLayoutManager(gridLayoutManager);

                if (dragFragment != null) {
                    removeDragFragment();
                }
            } else if (currentQuestion.optionsType.equals("image")) {
                // Set the adapter
                AnswerAdapter adapter = new AnswerAdapter(currentQuestion.options, this::onAnswerClicked);
                answersRecyclerView.setAdapter(adapter);

                // Set up GridLayoutManager with 2 columns
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                answersRecyclerView.setLayoutManager(gridLayoutManager);

                if (dragFragment != null) {
                    removeDragFragment();
                }
            } else if (currentQuestion.optionsType.equals("drag")) {
                loadDragFragment(currentQuestion);

                answersRecyclerViewSection.setVisibility(View.GONE);
            }


        }
    }

    private void onAnswerClicked(Answer answer) {
        QuizActivityQuestion currentQuestion = quiz.get(currentQuestionIndex - 1);

        if (answer.answerName.equals(currentQuestion.correctAnswer)) {
            AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 0, false, null);
            correctAnswerCount++;
        } else {
            AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 1, true, null);
        }
    }

    private String calculateMarks() {
        float marks = ((float) correctAnswerCount / quiz.size()) * 100;
        Log.d("Emomarks", String.valueOf(marks));
        return marks + "%";
    }

    private void startQuestionRunnable() {
        if (questionRunnable == null) {
            questionRunnable = this::runQuestionSequence;
            handler.post(questionRunnable);
        }
    }

    private void stopQuestionRunnable() {
        if (questionRunnable != null) {
            handler.removeCallbacks(questionRunnable);
            stopTimeRunnable();
        }
    }

    private void runQuestionSequence() {
        if (currentQuestionIndex < allQuestionCount) {
            updateQuestion();
            currentQuestionIndex++;
            currentQuestionNoText.setText(String.valueOf(currentQuestionIndex));

            stopTimeRunnable();
            startTimeRunnable(); // Start the timer for the current question

            handler.postDelayed(questionRunnable, INTERVAL); // Move to the next question after the interval
        } else {
            stopQuestionRunnable(); // Stop if all questions are completed
        }
    }


    private void startTimeRunnable() {
        if (timeRunnable == null) {
            timeRunnable = this::runTimeSequence;
        }
        secondsRemaining = TOTAL_TIME_FOR_Q; // Reset the timer for each question
        handler.post(timeRunnable); // Use the existing handler
    }

    private void stopTimeRunnable() {
        if (timeRunnable != null) {
            handler.removeCallbacks(timeRunnable);
            timeRunnable = null; // Clear the reference to avoid duplicate runs
            AlertDialogUtil.dismissQuizDialog();
        }
    }

    private void runTimeSequence() {
        if (secondsRemaining > 0) {
            secondsRemainingView.setText(String.valueOf(secondsRemaining));
            secondsRemaining--;
            handler.postDelayed(timeRunnable, 1000); // Repeat every second
        } else {
            stopTimeRunnable(); // Stop the timer when it hits zero
        }
    }


    private void addCameraFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.camera_fragment_container, cameraFragment);
        transaction.commitAllowingStateLoss();
    }

    private void removeCameraFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(cameraFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopQuestionRunnable();
        stopTimeRunnable(); // Ensure the timeRunnable is also stopped
        removeCameraFragment();
    }

}
