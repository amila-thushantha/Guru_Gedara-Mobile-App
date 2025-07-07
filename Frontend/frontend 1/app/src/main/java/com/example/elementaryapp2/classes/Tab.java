package com.example.elementaryapp2.classes;

public class Tab {
    public int image;
    public String header;
    public String subText;

    public String pageType;
    public int pageSubtype;
    public int bgClr;
    public int btnBgClr;

    public Quiz quiz;


    public Tab(int image, String header, String subText, String pageType, int pageSubtype, int bgClr, int btnBgClr, Quiz quiz) {
        this.image = image;
        this.header = header;
        this.subText = subText;
        this.pageType = pageType;
        this.pageSubtype = pageSubtype;
        this.bgClr = bgClr;
        this.btnBgClr = btnBgClr;
        this.quiz = quiz;
    }
}
