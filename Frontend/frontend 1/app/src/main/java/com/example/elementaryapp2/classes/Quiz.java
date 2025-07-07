package com.example.elementaryapp2.classes;

import java.io.Serializable;
import java.util.List;

public class Quiz implements Serializable {
    public String quizName;
    public Integer tutorialResource;
    public List<QuizQuestion> quizQuestions;
    public Boolean audioEnabled = false;

    public Quiz(String quizName, Integer tutorialResource, List<QuizQuestion> quizQuestions) {
        this.quizName = quizName;
        this.tutorialResource = tutorialResource;
        this.quizQuestions = quizQuestions;
    }

    public Quiz(String quizName, Integer tutorialResource, List<QuizQuestion> quizQuestions, Boolean audioEnabled) {
        this.quizName = quizName;
        this.tutorialResource = tutorialResource;
        this.quizQuestions = quizQuestions;
        this.audioEnabled = audioEnabled;
    }
}
