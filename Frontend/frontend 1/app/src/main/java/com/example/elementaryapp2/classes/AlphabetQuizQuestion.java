package com.example.elementaryapp2.classes;

import java.util.List;

public class AlphabetQuizQuestion {
    public String correctAnswer;
    public List<String> slicedLetters;

    public AlphabetQuizQuestion(String correctAnswer, List<String> slicedLetters) {
        this.correctAnswer = correctAnswer;
        this.slicedLetters = slicedLetters;
    }
}
