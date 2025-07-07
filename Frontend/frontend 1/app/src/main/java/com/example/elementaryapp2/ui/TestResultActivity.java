package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.services.Services;

public class TestResultActivity extends AppCompatActivity {

    Button closeBtn;
    TextView sinhalaMarks, mathsMarks, sinhalaCorrectCount, mathsCorrectCount, prediciton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        // disable back press
        OnBackPressedCallback callbackBackPress = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callbackBackPress);

        String sinhalaMarksGET = String.valueOf(getIntent().getIntExtra("sinhalaMarks", 0));
        String mathsMarksGET = String.valueOf(getIntent().getIntExtra("mathsMarks", 0));

        String sinhalaCorrectCountGET = getIntent().getStringExtra("sinhalaCorrectCount");
        String mathsCorrectCountGET = getIntent().getStringExtra("mathsCorrectCount");
        String predictionGET = getIntent().getStringExtra("prediction");

        sinhalaMarks = findViewById(R.id.sinhalaExamView);
        mathsMarks = findViewById(R.id.mathsExamView);
        sinhalaCorrectCount = findViewById(R.id.sinhalaCorrectCount);
        mathsCorrectCount = findViewById(R.id.mathsCorrectCount);
        prediciton = findViewById(R.id.prediction);

        sinhalaMarks.setText(sinhalaMarksGET+"%");
        mathsMarks.setText(mathsMarksGET+"%");
        sinhalaCorrectCount.setText(sinhalaCorrectCountGET);
        mathsCorrectCount.setText(mathsCorrectCountGET);
        prediciton.setText(predictionGET);

        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestResultActivity.this, MenuMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}