package com.example.elementaryapp2.classes.face_activity_classes;

import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.drag_classes.DragQuestion;

import java.io.Serializable;
import java.util.List;

public class QuizActivityQuestion implements Serializable {
    public String question;
    public List<Answer> options;
    public String correctAnswer;
    public String optionsType; // image, text, drag
    public Integer imageResource;
    public DragQuestion dragQuestion;
    public int time = 0;

    public QuizActivityQuestion(String question, List<Answer> options, String correctAnswer, String optionsType, Integer imageResource) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.optionsType = optionsType;
        this.imageResource = imageResource;
        this.time = 10;
    }

    public QuizActivityQuestion(String question, DragQuestion dragQuestion, String optionsType, Integer imageResource) {
        this.question = question;
        this.dragQuestion = dragQuestion;
        this.optionsType = optionsType;
        this.imageResource = imageResource;
        this.time = 20;
    }
}
