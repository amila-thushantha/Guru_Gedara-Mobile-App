package com.example.elementaryapp2.classes;

public class Letter {
    public String letter;
    public String sinhala;
    public int index;

    public Letter(String letter, String sinhala, int index) {
        this.letter = letter;
        this.sinhala = sinhala;
        this.index = index;
    }

    public Letter(String letter) {
        this.letter = letter;
    }
}
