package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.services.Services;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestWelcomeActivity extends AppCompatActivity {

    Button startBtn;
    EditText mathsInput, sinhalaInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_welcome);

        // disable back press
        OnBackPressedCallback callbackBackPress = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callbackBackPress);

        mathsInput = findViewById(R.id.mathsMarkInput);
        sinhalaInput = findViewById(R.id.sinhalaMarkInput);

        startBtn = findViewById(R.id.buttonStart);
        startBtn.setOnClickListener(v -> {
            String mathsInputText = mathsInput.getText().toString();
            String sinhalaInputText = sinhalaInput.getText().toString();

            // Flag to track validation errors
            AtomicBoolean hasError = new AtomicBoolean(false);

            ValidationErrorCallback callback = isError -> {
                if (isError) {
                    hasError.set(true); // Set flag to true if there's an error
                } else {
                    hasError.set(false);
                }
            };

            // Validate inputs
            int mathsMarks = validateValue(mathsInputText, callback);
            int sinhalaMarks = validateValue(sinhalaInputText, callback);

            // Only proceed if there are no errors
            if (!hasError.get()) {
                Intent intent = new Intent(TestWelcomeActivity.this, TestLevelActivity.class);
                intent.putExtra("mathsMarks", mathsMarks);
                intent.putExtra("sinhalaMarks", sinhalaMarks);
                startActivity(intent);
                finish();
            }
        });
    }

    public interface ValidationErrorCallback {
        void onError(Boolean isError);
    }

    private int validateValue(String value, ValidationErrorCallback callback) {
        int marks = 0; // Default value in case of parsing failure
        if (!value.isEmpty()) { // Check if input is not empty
            try {
                marks = Integer.parseInt(value);
                callback.onError(false);
            } catch (NumberFormatException e) {
                String errorMessage = "Invalid input: " + value;
                Toast.makeText(TestWelcomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                if (callback != null) {
                    callback.onError(true);
                }
            }
        } else {
            String errorMessage = "Marks Empty";
            Toast.makeText(TestWelcomeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            if (callback != null) {
                callback.onError(true);
            }
        }
        return marks;
    }
}
