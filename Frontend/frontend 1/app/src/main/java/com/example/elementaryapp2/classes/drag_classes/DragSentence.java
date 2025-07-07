package com.example.elementaryapp2.classes.drag_classes;

import android.os.Parcelable;

import java.io.Serializable;

public class DragSentence implements Serializable {
    public String p1;
    public String p2;
    public String correctAnswer;

    public DragSentence(String p1, String p2, String correctAnswer) {
        this.p1 = p1;
        this.p2 = p2;
        this.correctAnswer = correctAnswer;
    }
}
