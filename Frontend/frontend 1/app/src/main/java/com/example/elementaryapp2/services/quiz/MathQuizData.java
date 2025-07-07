package com.example.elementaryapp2.services.quiz;

import android.graphics.Color;

import com.example.elementaryapp2.classes.MathQuizQuestion;
import com.example.elementaryapp2.classes.ShapeBox;
import com.example.elementaryapp2.services.shape.ShapeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MathQuizData {
    public static List<MathQuizQuestion> mathQuiz = new ArrayList<>();
    static {
        mathQuiz.add(new MathQuizQuestion(Color.BLUE, ShapeView.SHAPE_CIRCLE, 5, 8, '+'));
        mathQuiz.add(new MathQuizQuestion(Color.RED, ShapeView.SHAPE_TRIANGLE, 3, 5, '+'));
        mathQuiz.add(new MathQuizQuestion(Color.GREEN, ShapeView.SHAPE_CIRCLE, 9, 2, '-'));
        mathQuiz.add(new MathQuizQuestion(Color.YELLOW, ShapeView.SHAPE_SQUARE, 9, 9, '+'));
        mathQuiz.add(new MathQuizQuestion(Color.MAGENTA, ShapeView.SHAPE_TRIANGLE, 1, 2, '+'));
    }

    public static List<ShapeBox> getAnswers(MathQuizQuestion question) {
        Random random = new Random();
        List<ShapeBox> answers = new ArrayList<>();
        List<Integer> shapeList = Arrays.asList(ShapeView.SHAPE_SQUARE, ShapeView.SHAPE_CIRCLE, ShapeView.SHAPE_TRIANGLE);
        List<Integer> colorList = Arrays.asList(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA);

        int result = 0;
        switch (question.operation) {
            case '+':
                result = question.op_1 + question.op_2;
                break;
            case '-':
                result = question.op_1 - question.op_2;
                break;
        }

        // add correct answer
        answers.add(new ShapeBox(question.shape, question.shapeColor, result, true));

        // add incorrect answers
        // add incorrect shapes
        List<Integer> availableShapes = new ArrayList<>(shapeList);
        availableShapes.remove(shapeList.indexOf(question.shape));

        for (int shape : availableShapes) {
            if (availableShapes.indexOf(shape) == availableShapes.size() - 1) {
                answers.add(new ShapeBox(shape, question.shapeColor, result - 1, false));
            } else {
                answers.add(new ShapeBox(shape, question.shapeColor, result, false));
            }
        }

        // add incorrect color
        List<Integer> availableColors = new ArrayList<>(colorList);
        availableColors.remove(colorList.indexOf(question.shapeColor));

        int randomIndex = random.nextInt(availableColors.size());
        int randomColor = availableColors.get(randomIndex);

        answers.add(new ShapeBox(question.shape, randomColor, result, false));

        Collections.shuffle(answers);
        return answers;
    }
}
