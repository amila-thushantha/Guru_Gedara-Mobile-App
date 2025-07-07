package com.example.elementaryapp2.classes;

import java.io.Serializable;
import java.util.List;

public class TestQuestion implements Serializable {
    public String questionText;
    public List<Answer> answers;
    public String correctAnswerName;
    public Integer imageResource;
    public String questionType;

    public TestQuestion(String questionText, List<Answer> answers, String correctAnswerName, Integer imageResource, String questionType) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerName = correctAnswerName;
        this.imageResource = imageResource;
        this.questionType = questionType;
    }
}
