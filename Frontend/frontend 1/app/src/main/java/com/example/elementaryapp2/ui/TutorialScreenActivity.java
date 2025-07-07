package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.classes.TuteMathQuestion;
import com.example.elementaryapp2.services.Services;

import java.io.Serializable;
import java.util.List;

public class TutorialScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);

        Services.onPressBack(this);

        Integer type = getIntent().getIntExtra("type", 0);
        Integer subtype = getIntent().getIntExtra("subtype", 0);
        Integer tutorialResource = getIntent().getIntExtra("tutorialResource", -1);

        Quiz sinhalaQuiz;
        List<TuteMathQuestion> mathQuiz;

        if (type == 0) {
            mathQuiz = null;
            sinhalaQuiz  = (Quiz) getIntent().getSerializableExtra("quizData");
        } else {
            sinhalaQuiz = null;
            if (type == 1) {
                mathQuiz = (List<TuteMathQuestion>) getIntent().getSerializableExtra("quizData");
            } else {
                mathQuiz = null;
            }
        }

        VideoView tutorialVideo = findViewById(R.id.tutorialVideo);
        Button skipButton = findViewById(R.id.skipButton);

        // Set video path to play from the raw folder
        Integer resourse = -1;
        if (type == 0) {
            if (sinhalaQuiz != null) {
                resourse = sinhalaQuiz.tutorialResource;
            }
        } else if (type == 1 || type == 2) {
            if (tutorialResource != -1) {
                resourse = tutorialResource;
            }
        }


        if (resourse != -1) {
            String videoPath = "android.resource://" + getPackageName() + "/" + resourse;
            tutorialVideo.setVideoPath(videoPath);

            // Add MediaController for playback controls
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(tutorialVideo);
            tutorialVideo.setMediaController(mediaController);

            // Start the video when it's prepared
            tutorialVideo.setOnPreparedListener(MediaPlayer::start);
        }

        // Set skip button listener
        skipButton.setOnClickListener(v -> {
            if (type == 0) {
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("quizData", sinhalaQuiz);
                startActivity(intent);
            } else if (type == 1) {
                Intent intent = new Intent(this, MathTuteActivity.class);
                intent.putExtra("quizData", (Serializable) mathQuiz);
                startActivity(intent);
            } else if (type == 2) {
                Intent intent = new Intent(this, SelectSubActivity.class);
                intent.putExtra("subActivityType", subtype);
                startActivity(intent);
            }

        });
    }
}