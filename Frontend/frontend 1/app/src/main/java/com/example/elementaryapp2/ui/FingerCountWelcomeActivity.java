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

public class FingerCountWelcomeActivity extends AppCompatActivity {

    Button startBtn;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_count_welcome);

        Services.onPressBack(this);

        startBtn = findViewById(R.id.buttonStart);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FingerCountWelcomeActivity.this, FingerCountActivity.class);
                intent.putExtra("ARG_PARAM_TYPE", "video");
                intent.putExtra("ARG_PARAM_SCREEN_NAME", "count_screen");
                startActivity(intent);
                finish();
            }
        });
    }
}