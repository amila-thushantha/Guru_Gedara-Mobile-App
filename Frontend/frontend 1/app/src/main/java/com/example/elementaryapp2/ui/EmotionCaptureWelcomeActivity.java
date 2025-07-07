package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.services.Services;

import java.io.Serializable;

public class EmotionCaptureWelcomeActivity extends AppCompatActivity {

    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_capture_welcome);

        Serializable quiz = getIntent().getSerializableExtra("quizData");

        Services.onPressBack(this);

        startBtn = findViewById(R.id.buttonNext);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmotionCaptureWelcomeActivity.this, EmotionCaptureActivity.class);
                intent.putExtra("ARG_PARAM_TYPE", "video_without_preview");
                intent.putExtra("ARG_PARAM_SCREEN_NAME", "emotion_screen");
                intent.putExtra("quiz", quiz);
                startActivity(intent);
                finish();
            }
        });
    }
}