package com.example.elementaryapp2.classes;

import com.example.elementaryapp2.classes.face_activity_classes.QuizActivityQuestion;

import java.util.List;

public class SubTab extends Tab{
    public List<TuteMathQuestion> problems;
    public List<QuizActivityQuestion> faceActivityProblems;
    public Integer tutorialResource;

    public SubTab(int image, String header, String subText, String pageType, int pageSubtype, int bgClr, int btnBgClr, List<TuteMathQuestion> problems, Integer tutorialResource) {
        super(image, header, subText, pageType, pageSubtype, bgClr, btnBgClr, null);
        this.problems = problems;
        this.tutorialResource = tutorialResource;
    }

    public SubTab(int image, String header, String subText, String pageType, int pageSubtype, int bgClr, int btnBgClr, List<QuizActivityQuestion> faceActivityProblems) {
        super(image, header, subText, pageType, pageSubtype, bgClr, btnBgClr, null);
        this.faceActivityProblems = faceActivityProblems;
    }
}
