package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.MathQuizQuestion;
import com.example.elementaryapp2.classes.ShapeBox;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.quiz.MathQuizData;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.recycler_view.MathQuizAnswerAdapter;
import com.example.elementaryapp2.services.shape.ShapeGenerator;

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

public class ShapeActivity extends AppCompatActivity {

    private ShapeGenerator shapeGenerator_1, shapeGenerator_2;
    private int totalquestions = 0;
    private int currentQuestionIndex = 0;
    private int correctCount = 0;
    private TextView tvTotalQuestions, tvCurrentQuestion, tvOperation;
    private RecyclerView answersRecyclerView;
    private List<MathQuizQuestion> mathQuiz = null;
    private int DIALOG_TIMEOUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);

        Services.onPressBack(this);

        LinearLayout operand_1 = findViewById(R.id.op_1);
        LinearLayout operand_2 = findViewById(R.id.op_2);

        tvTotalQuestions = findViewById(R.id.totalQuestionCount);
        tvCurrentQuestion = findViewById(R.id.currentQuestion);
        tvOperation = findViewById(R.id.operator);

        answersRecyclerView = findViewById(R.id.answersRecyclerView);

        mathQuiz = MathQuizData.mathQuiz;
        totalquestions = mathQuiz.size();
        tvTotalQuestions.setText(String.valueOf(totalquestions));

        shapeGenerator_1 = new ShapeGenerator(this, operand_1);
        shapeGenerator_2 = new ShapeGenerator(this, operand_2);

        displayQuestion();
    }

    protected void nextQuestion() {
        if (currentQuestionIndex < totalquestions - 1) {
            currentQuestionIndex = currentQuestionIndex + 1;
            displayQuestion();
        }
    }

    protected void displayQuestion() {
        MathQuizQuestion currentQuestion = mathQuiz.get(currentQuestionIndex);
        tvCurrentQuestion.setText(String.valueOf(currentQuestionIndex + 1));
        tvOperation.setText(String.valueOf(currentQuestion.operation));
        shapeGenerator_1.generateShapes(currentQuestion.shape, currentQuestion.shapeColor, currentQuestion.op_1);
        shapeGenerator_2.generateShapes(currentQuestion.shape, currentQuestion.shapeColor, currentQuestion.op_2);

        List<ShapeBox> answers = MathQuizData.getAnswers(currentQuestion);
        // Set the adapter
        MathQuizAnswerAdapter adapter = new MathQuizAnswerAdapter(answers, this::onAnswerClicked, this);
        answersRecyclerView.setAdapter(adapter);

        // Set up GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        answersRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void onAnswerClicked(ShapeBox answer) {
        if (answer.isCorrect) {
            correctCount = correctCount + 1;
            if (currentQuestionIndex == mathQuiz.size() - 1) {
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
                        if (currentQuestionIndex < mathQuiz.size() - 1) {
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
                    if (currentQuestionIndex < mathQuiz.size() - 1) {
                        currentQuestionIndex = currentQuestionIndex + 1;
                        displayQuestion();
                    }
                }
            }, DIALOG_TIMEOUT);
        }
    }

    private void calculate() {
        float marks = ((float) correctCount / totalquestions) * 100;
        float roundedMarks = Math.round(marks * 100.0f) / 100.0f;
        if (currentQuestionIndex == mathQuiz.size() - 1) {
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
                .addFormDataPart("activityName", "mathQuiz");

        String url = Services.ipAddress + "save_marks";
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(ShapeActivity.this, "Connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(ShapeActivity.this, MenuMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ShapeActivity.this, ""+errorDesc, Toast.LENGTH_SHORT).show();
                            }
                            AlertDialogUtil.dismissLoadingDialog();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(ShapeActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        AlertDialogUtil.dismissLoadingDialog();
                    });
                }
            }
        });
    }
}

