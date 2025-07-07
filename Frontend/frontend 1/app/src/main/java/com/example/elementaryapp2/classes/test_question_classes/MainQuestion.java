package com.example.elementaryapp2.classes.test_question_classes;

import java.util.List;

public class MainQuestion {
    public String mainQuestion;
    public List<SubQuestion> subQuestions;

    public MainQuestion(String mainQuestion, List<SubQuestion> subQuestions) {
        this.mainQuestion = mainQuestion;
        this.subQuestions = subQuestions;
    }
}
