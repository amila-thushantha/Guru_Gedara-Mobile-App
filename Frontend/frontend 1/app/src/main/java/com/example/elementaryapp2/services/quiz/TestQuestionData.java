package com.example.elementaryapp2.services.quiz;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.TestQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestQuestionData {
    static List<Answer> answers_1 = Arrays.asList(
            new Answer("True", null),
            new Answer("False", null)
    );

    static List<Answer> answers_2 = Arrays.asList(
            new Answer("1", null),
            new Answer("2", null),
            new Answer("4", null),
            new Answer("6", null)
    );

    static List<Answer> answers_3 = Arrays.asList(
            new Answer("3", null),
            new Answer("2", null),
            new Answer("5", null),
            new Answer("8", null)
    );

    public static List<TestQuestion> testQuestions = new ArrayList<>();

    static  {
        testQuestions.add(new TestQuestion("Select Number One.", answers_2, "1", null, "math"));
        testQuestions.add(new TestQuestion("Select Number Six.", answers_2, "6", null, "math"));
        testQuestions.add(new TestQuestion("Select Number Three.", answers_3, "3", null, "math"));
        testQuestions.add(new TestQuestion("Select Number Eight.", answers_3, "8", null, "math"));

        testQuestions.add(new TestQuestion("Is below sinhala letter written correctly?", answers_1, "True", R.drawable.finger_0, "sinhala"));
        testQuestions.add(new TestQuestion("Is below sinhala letter written correctly?", answers_1, "True", R.drawable.finger_1, "sinhala"));
        testQuestions.add(new TestQuestion("Is below sinhala letter written correctly?", answers_1, "False", R.drawable.finger_2, "sinhala"));
        testQuestions.add(new TestQuestion("Is below sinhala letter written correctly?", answers_1, "True", R.drawable.finger_3, "sinhala"));
    }
}
