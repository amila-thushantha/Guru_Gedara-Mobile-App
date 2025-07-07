package com.example.elementaryapp2.classes.test_question_classes;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public class ResultResponse {
    public int totalMathsQuestions;
    public int totalSinhalaQuestions;

    public int incorrectMathsCount;
    public int incorrectSinhalaCount;

    public ResultResponse(int totalMathsQuestions, int totalSinhalaQuestions, int incorrectMathsCount, int incorrectSinhalaCount) {
        this.totalMathsQuestions = totalMathsQuestions;
        this.totalSinhalaQuestions = totalSinhalaQuestions;
        this.incorrectMathsCount = incorrectMathsCount;
        this.incorrectSinhalaCount = incorrectSinhalaCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResultResponse{" +
                "totalMathsQuestions=" + totalMathsQuestions +
                ", totalSinhalaQuestions=" + totalSinhalaQuestions +
                ", incorrectMathsCount=" + incorrectMathsCount +
                ", incorrectSinhalaCount=" + incorrectSinhalaCount +
                '}';
    }

}
