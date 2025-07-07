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

public class QuizWelcomeActivity extends AppCompatActivity {

    Button startBtn;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_welcome);

        Services.onPressBack(this);

        Serializable quiz = getIntent().getSerializableExtra("quizData");
        Integer type = getIntent().getIntExtra("type", 0);
        Integer subtype = getIntent().getIntExtra("subtype", 0);
        Integer tutorialResource = getIntent().getIntExtra("tutorialResource", -1);

        startBtn = findViewById(R.id.buttonStart);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizWelcomeActivity.this, TutorialScreenActivity.class);
                intent.putExtra("quizData", quiz);
                intent.putExtra("type", type);
                intent.putExtra("subtype", subtype);
                intent.putExtra("tutorialResource", tutorialResource);
                startActivity(intent);
                finish();
            }
        });
    }
}