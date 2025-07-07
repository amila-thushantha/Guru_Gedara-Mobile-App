package com.example.elementaryapp2.classes.test_question_classes;

import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.MathEquation;

import java.util.List;

public class SubQuestion {
    public String subQuestion;
    public String correctAnswer;
    public String questionType; //normal, withImage
    public Integer imageResource = null;

    public List<Answer> options;
    public String optionType; //image, text, enter, calc
    public MathEquation mathEquation;
    public String category; //maths, sinhala

    public SubQuestion(String category, String subQuestion, String correctAnswer, String questionType, List<Answer> options, String optionType, MathEquation mathEquation) {
        this.category = category;
        this.subQuestion = subQuestion;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType;
        this.options = options;
        this.optionType = optionType;
        this.mathEquation = mathEquation;
    }

    public SubQuestion(String category, String subQuestion, String correctAnswer, String questionType, Integer imageResource, List<Answer> options, String optionType, MathEquation mathEquation) {
        this.category = category;
        this.subQuestion = subQuestion;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType;
        this.imageResource = imageResource;
        this.options = options;
        this.optionType = optionType;
        this.mathEquation = mathEquation;
    }
}
