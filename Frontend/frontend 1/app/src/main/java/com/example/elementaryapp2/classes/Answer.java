package com.example.elementaryapp2.classes;

import java.io.Serializable;

public class Answer implements Serializable {
    public String answerName;
    public Integer resource;

    public Answer(String answerName, Integer resource) {
        this.answerName = answerName;
        this.resource = resource;
    }
}
