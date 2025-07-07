package com.example.elementaryapp2.classes.drag_classes;

import android.os.Parcelable;

import com.example.elementaryapp2.classes.Answer;

import java.io.Serializable;
import java.util.List;

public class DragQuestion implements Serializable {
    public List<DragSentence> sentences;
    public List<Answer> options;

    public DragQuestion(List<DragSentence> sentences, List<Answer> options) {
        this.sentences = sentences;
        this.options = options;
    }
}
