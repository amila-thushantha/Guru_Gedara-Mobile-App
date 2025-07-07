package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.classes.QuizQuestion;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.recycler_view.AnswerAdapter;

import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private Integer currentQuestionIndex = 0;
    RecyclerView answersRecyclerView;
    TextView questionText, quizName, totalQuestionCountText, currentQuestionNoText;
    Quiz quiz = null;
    private MediaPlayer mediaPlayer;
    private Button playAudioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quiz = (Quiz) getIntent().getSerializableExtra("quizData");

        Services.onPressBack(this);

        answersRecyclerView = findViewById(R.id.answersRecyclerView);
        questionText = findViewById(R.id.questionText);
        totalQuestionCountText = findViewById(R.id.totalQuestionCount);
        currentQuestionNoText = findViewById(R.id.currentQuestion);
        quizName = findViewById(R.id.quizName);
        playAudioButton = findViewById(R.id.playBtn);

        playAudioButton.setVisibility(View.GONE);

        updateQuestion();
    }

    private void updateQuestion() {
        if (quiz != null) {
            quizName.setText(quiz.quizName);

            QuizQuestion currentQuestion = quiz.quizQuestions.get(currentQuestionIndex);
            questionText.setText(currentQuestion.questionText);

            currentQuestionNoText.setText(String.valueOf(currentQuestionIndex + 1));
            totalQuestionCountText.setText(String.valueOf(quiz.quizQuestions.size()));

            // Set the adapter
            AnswerAdapter adapter = new AnswerAdapter(currentQuestion.answers, this::onAnswerClicked);
            answersRecyclerView.setAdapter(adapter);

            // Set up GridLayoutManager with 2 columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            answersRecyclerView.setLayoutManager(gridLayoutManager);

            if (quiz.audioEnabled) {
                if (currentQuestion.audioResource != null) {
                    // Set up the audio file
                    mediaPlayer = MediaPlayer.create(this, currentQuestion.audioResource);

                    playAudioButton.setVisibility(View.VISIBLE);

                    // Play audio when button is clicked
                    playAudioButton.setOnClickListener(v -> {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0); // Reset to the start
                        } else {
                            mediaPlayer.start();
                        }
                    });
                }
            }
        }
    }

    private void onAnswerClicked(Answer answer) {
        QuizQuestion currentQuestion = quiz.quizQuestions.get(currentQuestionIndex);
        if (answer.answerName.equals(currentQuestion.correctAnswerName)) {
            if (currentQuestionIndex == quiz.quizQuestions.size() - 1) {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 2, true, isClicked -> {
                    if (isClicked) {
                        Intent intent = new Intent(QuizActivity.this, MenuMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 0,true, isClicked -> {
                    if (isClicked) {
                        if (currentQuestionIndex < quiz.quizQuestions.size() - 1) {
                            currentQuestionIndex = currentQuestionIndex + 1;
                            updateQuestion();
                        }
                    }
                });
            }
        } else {
            AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 1,true, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
