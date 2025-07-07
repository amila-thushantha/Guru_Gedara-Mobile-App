package com.example.elementaryapp2.classes;

public class MathQuizQuestion {
    public int shape;
    public int shapeColor;
    public int op_1;
    public int op_2;
    public char operation;

    public MathQuizQuestion(int shapeColor, int shape, int op_1, int op_2, char operation) {
        this.shape = shape;
        this.shapeColor = shapeColor;
        this.op_1 = op_1;
        this.op_2 = op_2;
        this.operation = operation;
    }
}
