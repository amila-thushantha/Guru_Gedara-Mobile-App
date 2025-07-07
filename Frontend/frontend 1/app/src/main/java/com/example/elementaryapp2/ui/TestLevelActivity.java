package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.test_question_classes.MainQuestion;
import com.example.elementaryapp2.classes.test_question_classes.ResultResponse;
import com.example.elementaryapp2.classes.test_question_classes.TestLevel;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.quiz.TestLevelQuestionData;
import com.example.elementaryapp2.services.recycler_view.RecyclerViewAdapterSubTabs;
import com.example.elementaryapp2.services.recycler_view.SubQuestionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestLevelActivity extends AppCompatActivity implements SubQuestionAdapter.AdapterCallback {

    TextView tvMainQuestion, tvLevel;
    RecyclerView contentRecyclerView;
    Button nextBtn;

    private SubQuestionAdapter adapter;

    List<TestLevel> quiz = null;

    private int totalquestions = 0;
    private int currentLevelIndex = 0;
    private int currentQuestionIndex = 0;
    private int correctCount = 0;
    private TextView tvTotalQuestions, tvCurrentQuestion, tvOperation;

    int mathsIncorrectCount = 0;
    int sinhalaIncorrectCount = 0;

    long mathCount = 0;
    long sinhalaCount = 0;

    int mathsMarks = 0;
    int sinhalaMarks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_level);

        Services.onPressBack(this);

        mathsMarks = getIntent().getIntExtra("mathsMarks", 0);
        sinhalaMarks = getIntent().getIntExtra("sinhalaMarks", 0);

        quiz = TestLevelQuestionData.testLevelQuestionData;

        tvLevel = findViewById(R.id.tvLevel);
        tvMainQuestion = findViewById(R.id.tvMainQuestion);
        contentRecyclerView = findViewById(R.id.contentRecyclerView);
        nextBtn = findViewById(R.id.btn_next);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.calculateMarks();
            }
        });

        tvTotalQuestions = findViewById(R.id.totalQuestionCount);
        tvCurrentQuestion = findViewById(R.id.currentQuestion);

        displayQuestion();
    }

    private void displayQuestion() {
        TestLevel currentLevel = quiz.get(currentLevelIndex);
        tvLevel.setText(currentLevel.levelName);

        MainQuestion currentMainQuestion = currentLevel.mainQuestions.get(currentQuestionIndex);
        tvMainQuestion.setText(currentMainQuestion.mainQuestion);

        totalquestions = currentMainQuestion.subQuestions.size();
        tvTotalQuestions.setText(String.valueOf(totalquestions));
        tvCurrentQuestion.setText(String.valueOf(currentQuestionIndex + 1));

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);

        contentRecyclerView.setLayoutManager(layoutManager1);
        adapter = new SubQuestionAdapter(this, currentMainQuestion.subQuestions, this);
        contentRecyclerView.setAdapter(adapter);
    }

    private void nextQuestion() {
        if (currentQuestionIndex < totalquestions - 1) {
            currentQuestionIndex = currentQuestionIndex + 1;
            displayQuestion();
        } else {
            if (currentLevelIndex < quiz.size() - 1) {
                currentLevelIndex = currentLevelIndex + 1;
                currentQuestionIndex = 0;
                displayQuestion();
            } else if (currentLevelIndex == quiz.size() - 1) {
                calculateRatios();
            }
        }
    }

    private void calculateRatios() {
        float mathsIncorrectRatio = (float) mathsIncorrectCount / (mathCount * 2);
        float sinhalaIncorrectRatio = (float) sinhalaIncorrectCount / (sinhalaCount * 2);

        Log.d("testQuestion", String.valueOf(mathsIncorrectRatio));
        Log.d("testQuestion", String.valueOf(sinhalaIncorrectRatio));
        Log.d("testQuestion", String.valueOf(mathsIncorrectCount));
        Log.d("testQuestion", String.valueOf(sinhalaIncorrectCount));
        Log.d("testQuestion", String.valueOf(mathCount));
        Log.d("testQuestion", String.valueOf(sinhalaCount));

        sendToBackend(mathsIncorrectRatio, sinhalaIncorrectRatio);
    }

    @Override
    public void onDataSent(ResultResponse data) {
        Log.d("responseData", String.valueOf(data));
        mathCount = mathCount + data.totalMathsQuestions;
        sinhalaCount = sinhalaCount + data.totalSinhalaQuestions;

        mathsIncorrectCount = mathsIncorrectCount + data.incorrectMathsCount;
        sinhalaIncorrectCount = mathsIncorrectCount + data.incorrectSinhalaCount;

        nextQuestion();
    }

    private void sendToBackend(float mathsIncorrectRatio, float sinhalaIncorrectRatio) {
        AlertDialogUtil.showLoadingDialog(this, "Analyzing...");

        // Create an OkHttpClient with timeout settings
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", Services.userEmail)
                .addFormDataPart("mathsIncorrectRatio", String.valueOf(mathsIncorrectRatio))
                .addFormDataPart("sinhalaIncorrectRatio", String.valueOf(sinhalaIncorrectRatio))
                .addFormDataPart("mathsMarks", String.valueOf(mathsMarks))
                .addFormDataPart("sinhalaMarks", String.valueOf(sinhalaMarks));

        String url = Services.ipAddress + "get_weakness";
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(TestLevelActivity.this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String prediction = json.getString("prediction");

                            if (error.equals("0")) {
                                String mathsCorrectCount = (mathCount - mathsIncorrectCount) + "/" + mathCount;
                                String sinhalaCorrectCount = (sinhalaCount - sinhalaIncorrectCount) + "/" + sinhalaCount;

                                Intent intent = new Intent(TestLevelActivity.this, TestResultActivity.class);
                                intent.putExtra("mathsCorrectCount", mathsCorrectCount);
                                intent.putExtra("sinhalaCorrectCount", sinhalaCorrectCount);
                                intent.putExtra("mathsMarks", mathsMarks);
                                intent.putExtra("sinhalaMarks", sinhalaMarks);
                                intent.putExtra("prediction", prediction);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(TestLevelActivity.this, ""+errorDesc, Toast.LENGTH_SHORT).show();
                            }
                            AlertDialogUtil.dismissLoadingDialog();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(TestLevelActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        AlertDialogUtil.dismissLoadingDialog();
                    });
                }
            }
        });
    }
}