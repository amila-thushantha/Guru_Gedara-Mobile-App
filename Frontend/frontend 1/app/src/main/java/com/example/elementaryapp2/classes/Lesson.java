package com.example.elementaryapp2.classes;

public class Lesson {
    public int image;
    public String header;
    public String subText;

    public String pageType;

    public int subType;

    public Lesson(int image, String header, String subText, String pageType, int subType) {
        this.image = image;
        this.header = header;
        this.subText = subText;
        this.pageType = pageType;
        this.subType = subType;
    }
}
