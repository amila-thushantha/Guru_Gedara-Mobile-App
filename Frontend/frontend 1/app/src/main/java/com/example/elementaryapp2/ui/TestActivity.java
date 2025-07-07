package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.TestQuestion;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.quiz.TestQuestionData;
import com.example.elementaryapp2.services.recycler_view.ChoicesAdapter;

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

public class TestActivity extends AppCompatActivity {

    private Integer currentQuestionIndex = 0;
    RecyclerView answersRecyclerView;
    TextView questionText, totalQuestionCountText, currentQuestionNoText;
    List<TestQuestion> quiz = null;
    ImageView image;
    CardView imageContainer;

    int mathsIncorrectCount = 0;
    int sinhalaIncorrectCount = 0;

    long mathCount = 0;
    long sinhalaCount = 0;

    int mathsMarks = 0;
    int sinhalaMarks = 0;

    int DIALOG_TIMEOUT = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        quiz = TestQuestionData.testQuestions;
        mathsMarks = getIntent().getIntExtra("mathsMarks", 0);
        sinhalaMarks = getIntent().getIntExtra("sinhalaMarks", 0);

        if (quiz != null) {
            mathCount = quiz.stream()
                    .filter(question -> "math".equals(question.questionType))
                    .count();

            sinhalaCount = quiz.stream()
                    .filter(question -> "sinhala".equals(question.questionType))
                    .count();

        }

        // disable back press
        OnBackPressedCallback callbackBackPress = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callbackBackPress);

        answersRecyclerView = findViewById(R.id.answersRecyclerView);
        questionText = findViewById(R.id.questionText);
        totalQuestionCountText = findViewById(R.id.totalQuestionCount);
        currentQuestionNoText = findViewById(R.id.currentQuestion);
        image = findViewById(R.id.answerImage);
        imageContainer = findViewById(R.id.image);

        updateQuestion();
    }

    private void updateQuestion() {
        if (quiz != null) {

            TestQuestion currentQuestion = quiz.get(currentQuestionIndex);
            questionText.setText(currentQuestion.questionText);

            currentQuestionNoText.setText(String.valueOf(currentQuestionIndex + 1));
            totalQuestionCountText.setText(String.valueOf(quiz.size()));

            if (currentQuestion.imageResource != null) {
                image.setImageResource(currentQuestion.imageResource);
                imageContainer.setVisibility(View.VISIBLE);
            } else {
                imageContainer.setVisibility(View.GONE);
            }

            // Set the adapter
            ChoicesAdapter adapter = new ChoicesAdapter(currentQuestion.answers, this::onAnswerClicked);
            answersRecyclerView.setAdapter(adapter);

            // Set up GridLayoutManager with 2 columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            answersRecyclerView.setLayoutManager(gridLayoutManager);
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

    private void onAnswerClicked(Answer answer) {
        TestQuestion currentQuestion = quiz.get(currentQuestionIndex);
        if (answer.answerName.equals(currentQuestion.correctAnswerName)) {
            if (currentQuestionIndex == quiz.size() - 1) {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 2, false, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialogUtil.dismissQuizDialog();
                        calculateRatios();
                    }
                }, DIALOG_TIMEOUT);
            } else {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 0, false, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialogUtil.dismissQuizDialog();
                        if (currentQuestionIndex < quiz.size() - 1) {
                            currentQuestionIndex = currentQuestionIndex + 1;
                            updateQuestion();
                        }
                    }
                }, DIALOG_TIMEOUT);
            }
        } else {
            if (currentQuestion.questionType.equals("math")) {
                mathsIncorrectCount = mathsIncorrectCount + 1;
            } else {
                sinhalaIncorrectCount = sinhalaIncorrectCount + 1;
            }

            if (currentQuestionIndex == quiz.size() - 1) {
                calculateRatios();
            }

            AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 1, false, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialogUtil.dismissQuizDialog();
                    if (currentQuestionIndex < quiz.size() - 1) {
                        currentQuestionIndex = currentQuestionIndex + 1;
                        updateQuestion();
                    }
                }
            }, DIALOG_TIMEOUT);
        }
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
                    Toast.makeText(TestActivity.this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                                Intent intent = new Intent(TestActivity.this, TestResultActivity.class);
                                intent.putExtra("mathsCorrectCount", mathsCorrectCount);
                                intent.putExtra("sinhalaCorrectCount", sinhalaCorrectCount);
                                intent.putExtra("mathsMarks", mathsMarks);
                                intent.putExtra("sinhalaMarks", sinhalaMarks);
                                intent.putExtra("prediction", prediction);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(TestActivity.this, ""+errorDesc, Toast.LENGTH_SHORT).show();
                            }
                            AlertDialogUtil.dismissLoadingDialog();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(TestActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        AlertDialogUtil.dismissLoadingDialog();
                    });
                }
            }
        });
    }
}
