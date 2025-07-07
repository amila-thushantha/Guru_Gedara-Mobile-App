package com.example.elementaryapp2.classes;

import java.io.Serializable;
import java.util.List;

public class TuteMathQuestion implements Serializable {
    public String name;
    public List<MathEquation> problems;

    public TuteMathQuestion(String name, List<MathEquation> problems) {
        this.name = name;
        this.problems = problems;
    }
}
