package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.AlphabetQuizQuestion;
import com.example.elementaryapp2.classes.Letter;
import com.example.elementaryapp2.classes.MathQuizQuestion;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.GridSpacingItemDecoration;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.quiz.AlphabetQuizData;
import com.example.elementaryapp2.services.recycler_view.RecycleViewAdapterLetters;
import com.example.elementaryapp2.services.recycler_view.RecyclerViewAdapterAlphabet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SinhalaAlphabetActivity extends AppCompatActivity {

    RecyclerViewAdapterAlphabet adapter;
    ArrayList<Letter> list, slicedList;
    TextView tvTotalQuestions, tvCurrentQuestion, tvSelectedWord, tvOriginalWord;
    Button checkBtn;
    RecyclerView recyclerView;
    Random random = new Random();

    private int totalquestions = 0;
    private int currentQuestionIndex = 0;
    private int correctCount = 0;

    private int DIALOG_TIMEOUT = 1000;

    private List<AlphabetQuizQuestion> alphabetQuiz = null;

    private String selectedWordM = "";

    // Sinhala alphabet in ISCII
    String[] sinhalaAlphabetISCII = {
            "w", "wd", "we", "wE", "b", "B",
            "W", "W!",
            "t", "tA", "ft", "T", "´", "T!",

            "l", "L", ".", ">", "Ù", "Õ",
            "p", "P", "c", "®", "[", "c",
            "g", "G", "v", "V", "K", "~",
            ";", ":", "o", "O", "k", "|",
            "m", "M", "n", "N", "u", "U",
            "h", "r", ",", "j",
            "Y", "I", "i", "y", "<", "*"
    };

    String[] sinhalaAlphabet = {
            "අ", "ආ", "ඇ", "ඈ", "ඉ", "ඊ",
            "උ", "ඌ",
            "එ", "ඒ", "ඓ", "ඔ", "ඕ", "ඖ",

            "ක", "ඛ", "ග", "ඝ", "ඞ", "ඟ",
            "ච", "ඡ", "ජ", "ඣ", "ඤ", "ඦ",
            "ට", "ඨ", "ඩ", "ඪ", "ණ", "ඬ",
            "ත", "ථ", "ද", "ධ", "න", "ඳ",
            "ප", "ඵ", "බ", "භ", "ම", "ඹ",
            "ය", "ර", "ල", "ව",
            "ශ", "ෂ", "ස", "හ", "ළ", "ෆ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinhala_alphabet);

        Services.onPressBack(this);
        alphabetQuiz = AlphabetQuizData.alphabetQuiz;

        tvSelectedWord = findViewById(R.id.tvWord);
        tvOriginalWord = findViewById(R.id.tvOriginalWord);
        tvTotalQuestions = findViewById(R.id.totalQuestionCount);
        tvCurrentQuestion = findViewById(R.id.currentQuestion);
        checkBtn = findViewById(R.id.btn_check);


        FloatingActionButton clearButton = findViewById(R.id.btn_clear);
        clearButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btnBack)));
        clearButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        recyclerView = findViewById(R.id.alphabetRecyclerView);

        list = new ArrayList<>();
        for (int i = 0; i <= sinhalaAlphabetISCII.length - 1; i++) {
            list.add(new Letter(sinhalaAlphabetISCII[i], sinhalaAlphabet[i], -1));
        }

        // Get the device screen width
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        // convert dp value to pixels
        int dpValue = 100; // 100dp
        float density = getResources().getDisplayMetrics().density;
        int itemWidth = (int) (dpValue * density);

        // Calculate the number of columns that can fit
        int spanCount = Math.max(1, screenWidth / itemWidth);

        // Set the GridLayoutManager with the calculated span count
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // Calculate spacing to center items
        int spacing = (screenWidth - (spanCount * itemWidth)) / (spanCount + 1);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearSelection();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedWordM.isEmpty()) {
                    handleCheck(selectedWordM.equals(alphabetQuiz.get(currentQuestionIndex).correctAnswer));
                }
            }
        });

        totalquestions = alphabetQuiz.size();
        tvTotalQuestions.setText(String.valueOf(totalquestions));
        displayQuestion();
    }

    private void handleCheck(boolean isCorrect) {
        if (isCorrect) {
            correctCount = correctCount + 1;
            if (currentQuestionIndex == alphabetQuiz.size() - 1) {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 2, false, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialogUtil.dismissQuizDialog();
                        calculate();
                    }
                }, DIALOG_TIMEOUT);
            } else {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 0, false, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialogUtil.dismissQuizDialog();
                        if (currentQuestionIndex < alphabetQuiz.size() - 1) {
                            currentQuestionIndex = currentQuestionIndex + 1;
                            displayQuestion();
                        }
                    }
                }, DIALOG_TIMEOUT);
            }
        } else {
            AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 1, false, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialogUtil.dismissQuizDialog();
                    calculate();
                    if (currentQuestionIndex < alphabetQuiz.size() - 1) {
                        currentQuestionIndex = currentQuestionIndex + 1;
                        displayQuestion();
                    }
                }
            }, DIALOG_TIMEOUT);
        }
    }

    private void createJumble(AlphabetQuizQuestion currentQuestion) {
        slicedList = new ArrayList<>();
        List<String> slicedLetters = currentQuestion.slicedLetters;
        int jumbleSize = 9;
        int remainingSize = jumbleSize - slicedLetters.size();

        for (int i = 0; i < slicedLetters.size(); i++) {
            slicedList.add(new Letter("", slicedLetters.get(i), -1));
        }

        for (int i = 0; i < remainingSize; i++) {
            Letter randomItem = list.get(random.nextInt(list.size()));
            slicedList.add(randomItem);
        }
        Collections.shuffle(slicedList);
        adapter = new RecyclerViewAdapterAlphabet(this, slicedList, recyclerView, R.color.bgClr_3_dark);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapterAlphabet.OnItemClickListener() {
            @Override
            public void onItemClick(String selectedWord) {
                tvSelectedWord.setText(selectedWord);
                selectedWordM = selectedWord;
            }
        });
    }

    private void displayQuestion() {
        AlphabetQuizQuestion currentQuestion = alphabetQuiz.get(currentQuestionIndex);
        tvOriginalWord.setText(currentQuestion.correctAnswer);
        tvCurrentQuestion.setText(String.valueOf(currentQuestionIndex + 1));
        createJumble(currentQuestion);
        adapter.clearSelection();
    }

    private void calculate() {
        float marks = ((float) correctCount / totalquestions) * 100;
        float roundedMarks = Math.round(marks * 100.0f) / 100.0f;
        if (currentQuestionIndex == alphabetQuiz.size() - 1) {
            sendToBackend(roundedMarks);
        }
    }

    private void sendToBackend(float marks) {
        AlertDialogUtil.showLoadingDialog(this, "Saving...");

        // Create an OkHttpClient with timeout settings
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", Services.userEmail)
                .addFormDataPart("marks", String.valueOf(marks))
                .addFormDataPart("activityName", "sinhalaQuiz");

        String url = Services.ipAddress + "save_marks";
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(SinhalaAlphabetActivity.this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    AlertDialogUtil.dismissLoadingDialog();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();

                    runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(responseString);
                            String error = json.getString("error");
                            String errorDesc = json.getString("errorDesc");

                            if (error.equals("0")) {
                                Intent intent = new Intent(SinhalaAlphabetActivity.this, MenuMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SinhalaAlphabetActivity.this, ""+errorDesc, Toast.LENGTH_SHORT).show();
                            }
                            AlertDialogUtil.dismissLoadingDialog();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(SinhalaAlphabetActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        AlertDialogUtil.dismissLoadingDialog();
                    });
                }
            }
        });
    }
}