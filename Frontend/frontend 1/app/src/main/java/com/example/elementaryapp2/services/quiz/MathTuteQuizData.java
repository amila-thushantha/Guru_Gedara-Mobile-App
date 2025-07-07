package com.example.elementaryapp2.services.quiz;

import com.example.elementaryapp2.classes.MathEquation;
import com.example.elementaryapp2.classes.TuteMathQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MathTuteQuizData {
    public static List<TuteMathQuestion> mathTuteQuiz1Data = new ArrayList<>();
    public static List<TuteMathQuestion> mathTuteQuiz2Data = new ArrayList<>();

    public static MathEquation problem_1_1_1 = new MathEquation(1, "+", 2, 3);
    public static MathEquation problem_1_1_2 = new MathEquation(6, "+", 3, 9);

    public static MathEquation problem_1_2_1 = new MathEquation(5, "-", 3, 2);
    public static MathEquation problem_1_2_2 = new MathEquation(8, "-", 4, 4);

    public static MathEquation problem_2_1_1 = new MathEquation(3, "x", 1, 3);
    public static MathEquation problem_2_1_2 = new MathEquation(2, "x", 4, 8);

    public static MathEquation problem_2_2_1 = new MathEquation(4, "/", 2, 2);
    public static MathEquation problem_2_2_2 = new MathEquation(8, "/", 2, 4);

    static {
        mathTuteQuiz1Data.add(new TuteMathQuestion("m<uq", new ArrayList<>(Arrays.asList(problem_1_1_1, problem_1_1_2))));
        mathTuteQuiz1Data.add(new TuteMathQuestion("m<uq", new ArrayList<>(Arrays.asList(problem_1_2_1, problem_1_2_2))));

        mathTuteQuiz2Data.add(new TuteMathQuestion("fojk", new ArrayList<>(Arrays.asList(problem_2_1_1, problem_2_1_2))));
        mathTuteQuiz2Data.add(new TuteMathQuestion("fojk", new ArrayList<>(Arrays.asList(problem_2_2_1, problem_2_2_2))));
    }
}
