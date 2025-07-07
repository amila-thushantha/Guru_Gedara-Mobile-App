package com.example.elementaryapp2.classes;

import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {
    public String questionText;
    public List<Answer> answers;
    public String correctAnswerName;
    public Integer audioResource;

    public QuizQuestion(String questionText, List<Answer> answers, String correctAnswerName) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerName = correctAnswerName;
    }

    public QuizQuestion(String questionText, List<Answer> answers, String correctAnswerName, Integer audioResource) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerName = correctAnswerName;
        this.audioResource = audioResource;
    }
}
