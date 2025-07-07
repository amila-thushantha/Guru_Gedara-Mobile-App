package com.example.elementaryapp2.classes.test_question_classes;

import java.util.List;

public class TestLevel {
    public String levelName;
    public List<MainQuestion> mainQuestions;

    public TestLevel(String levelName, List<MainQuestion> mainQuestions) {
        this.levelName = levelName;
        this.mainQuestions = mainQuestions;
    }
}
