package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.MathEquation;
import com.example.elementaryapp2.classes.MathQuizQuestion;
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.classes.TuteMathQuestion;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.Services;

import java.util.List;

public class MathTuteActivity extends AppCompatActivity {

    private TextView tv_op_1_1, tv_op_1_2, tv_op_2_1, tv_op_2_2, operator_1, operator_2;
    private EditText answer_1, answer_2;
    private Button checkBtn;

    private int totalquestions = 0;
    private int currentQuestionIndex = 0;
    private int correctCount = 0;
    private TextView tvTotalQuestions, tvCurrentQuestion, tvOperation;
    private List<TuteMathQuestion> mathQuiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_tute);

        Services.onPressBack(this);

        tv_op_1_1 = findViewById(R.id.op_1_1);
        tv_op_1_2 = findViewById(R.id.op_1_2);
        tv_op_2_1 = findViewById(R.id.op_2_1);
        tv_op_2_2 = findViewById(R.id.op_2_2);
        operator_1 = findViewById(R.id.operator_1);
        operator_2 = findViewById(R.id.operator_2);

        answer_1 = findViewById(R.id.answer_1);
        answer_2 = findViewById(R.id.answer_2);

        answer_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                answer_1.setTextColor(getResources().getColor(R.color.btnBlack));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        answer_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                answer_2.setTextColor(getResources().getColor(R.color.btnBlack));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvTotalQuestions = findViewById(R.id.totalQuestionCount);
        tvCurrentQuestion = findViewById(R.id.currentQuestion);

        checkBtn = findViewById(R.id.btn_check);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        mathQuiz = (List<TuteMathQuestion>) getIntent().getSerializableExtra("quizData");

        totalquestions = mathQuiz.size();
        tvTotalQuestions.setText(String.valueOf(totalquestions));

        displayQuestion();
    }

    private void displayQuestion() {
        TuteMathQuestion currentQuestion = mathQuiz.get(currentQuestionIndex);
        MathEquation set1 = currentQuestion.problems.get(0);
        MathEquation set2 = currentQuestion.problems.get(1);

        answer_1.setText("");
        answer_2.setText("");

        tv_op_1_1.setText(String.valueOf(set1.op_1));
        operator_1.setText(set1.operation);
        tv_op_1_2.setText(String.valueOf(set1.op_2));

        tv_op_2_1.setText(String.valueOf(set2.op_1));
        operator_2.setText(set2.operation);
        tv_op_2_2.setText(String.valueOf(set2.op_2));

        tvCurrentQuestion.setText(String.valueOf(currentQuestionIndex + 1));
    }

    private void nextQuestion() {
        if (checkAnswers()) {
            if (currentQuestionIndex == mathQuiz.size() - 1) {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 2, true, isClicked -> {
                    if (isClicked) {
                        Intent intent = new Intent(MathTuteActivity.this, SubPartActivity.class);
                        intent.putExtra("subPageType", 0);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 0,true, isClicked -> {
                    if (isClicked) {
                        if (currentQuestionIndex < mathQuiz.size() - 1) {
                            currentQuestionIndex = currentQuestionIndex + 1;
                            displayQuestion();
                        }
                    }
                });
            }
        }
    }

    private Boolean checkAnswers() {
        String answer1 = String.valueOf(answer_1.getText());
        String answer2 = String.valueOf(answer_2.getText());

        if (!answer1.isEmpty() && !answer2.isEmpty()) {
            String correctAnswer1 = String.valueOf(mathQuiz.get(currentQuestionIndex).problems.get(0).answer);
            String correctAnswer2 = String.valueOf(mathQuiz.get(currentQuestionIndex).problems.get(1).answer);

            if (!correctAnswer1.equals(answer1)) {
                answer_1.setTextColor(getResources().getColor(R.color.incorrect));
            }
            if (!correctAnswer2.equals(answer2)) {
                answer_2.setTextColor(getResources().getColor(R.color.incorrect));
            }

            if (!correctAnswer1.equals(answer1) || !correctAnswer2.equals(answer2)) {
                AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 1,true, null);
            }

            return correctAnswer1.equals(answer1) && correctAnswer2.equals(answer2);
        } else {
            AlertDialogUtil.ShowQuizAlertDialog(this, R.color.bgClr_1_dark, 1,true, null);
            return false;
        }
    }
}