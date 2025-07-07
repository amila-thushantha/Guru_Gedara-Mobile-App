package com.example.elementaryapp2.classes.test_question_classes;

public class TripleHashValue {
    public int item_id;
    public String selectedAnswer;
    public String correctAnswer;
    public String category;

    public TripleHashValue(int item_id, String selectedAnswer, String correctAnswer, String category) {
        this.item_id = item_id;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.category = category;
    }
}
