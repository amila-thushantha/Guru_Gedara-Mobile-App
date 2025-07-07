package com.example.elementaryapp2.classes;

import java.io.Serializable;

public class MathEquation implements Serializable {
    public int op_1;
    public String operation;
    public int op_2;
    public int answer;

    public MathEquation(int op_1, String operation, int op_2, int answer) {
        this.op_1 = op_1;
        this.operation = operation;
        this.op_2 = op_2;
        this.answer = answer;
    }
}
