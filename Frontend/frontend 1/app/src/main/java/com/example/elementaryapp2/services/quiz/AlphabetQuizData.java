package com.example.elementaryapp2.services.quiz;

import android.graphics.Color;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.AlphabetQuizQuestion;
import com.example.elementaryapp2.classes.MathQuizQuestion;
import com.example.elementaryapp2.services.shape.ShapeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphabetQuizData {
    public static List<AlphabetQuizQuestion> alphabetQuiz = new ArrayList<>();
    static {
        alphabetQuiz.add(new AlphabetQuizQuestion("මල", new ArrayList<>(Arrays.asList("ම", "ල"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("බය", new ArrayList<>(Arrays.asList("බ", "ය"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("ඉර", new ArrayList<>(Arrays.asList("ඉ", "ර"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("රට", new ArrayList<>(Arrays.asList("ර", "ට"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("අහස", new ArrayList<>(Arrays.asList("අ", "හ", "ස"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("මුහුද", new ArrayList<>(Arrays.asList("මු", "හු", "ද"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("කඩුව", new ArrayList<>(Arrays.asList("ක", "ඩු", "ව"))));
        alphabetQuiz.add(new AlphabetQuizQuestion("අලියා", new ArrayList<>(Arrays.asList("අ", "ලි", "යා"))));
    }
}
