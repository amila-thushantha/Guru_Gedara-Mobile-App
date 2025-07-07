package com.example.elementaryapp2.services.quiz;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.classes.QuizQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizData {
    // SET 1
    public static List<QuizQuestion> quizQuestions_1 = new ArrayList<>();
    static List<Answer> answers_1_1 = Arrays.asList(
            new Answer("බල්ලා", R.drawable.dog),
            new Answer("පූසා", R.drawable.cat),
            new Answer("කොටියා", R.drawable.tiger),
            new Answer("හාවා", R.drawable.rabbit)
    );
    static List<Answer> answers_1_2 = Arrays.asList(
            new Answer("පොල් ගස", R.drawable.coconut_tree),
            new Answer("රෝස මල", R.drawable.rose),
            new Answer("අරලිය මල්", R.drawable.araliya_flowers),
            new Answer("සූරියකාන්ත", R.drawable.sunflower)
    );
    static List<Answer> answers_1_3 = Arrays.asList(
            new Answer("පැන්සල", R.drawable.pencil),
            new Answer("පොත", R.drawable.book),
            new Answer("මේසය", R.drawable.table),
            new Answer("පුටුව", R.drawable.chair)
    );
    static List<Answer> answers_1_4 = Arrays.asList(
            new Answer("අහස", R.drawable.sky),
            new Answer("පාර", R.drawable.road),
            new Answer("වත්ත", R.drawable.garden),
            new Answer("ගඟ", R.drawable.river)
    );
    static List<Answer> answers_1_5 = Arrays.asList(
            new Answer("කපුටා", R.drawable.crow),
            new Answer("අශ්වයා", R.drawable.horse),
            new Answer("එළදෙන", R.drawable.cow),
            new Answer("අලියා", R.drawable.elephant)
    );
    static List<Answer> answers_1_6 = Arrays.asList(
            new Answer("ඇපල් ගෙඩිය", R.drawable.apple),
            new Answer("කෙසෙල් ගෙඩිය", R.drawable.banana),
            new Answer("අඹ ගෙඩිය", R.drawable.mango),
            new Answer("ගස්ලබු", R.drawable.papaya)
    );
    static List<Answer> answers_1_7 = Arrays.asList(
            new Answer("දොඩම්", R.drawable.orange),
            new Answer("කැරට්", R.drawable.carrot),
            new Answer("මිදි", R.drawable.grapes),
            new Answer("ජම්බු", R.drawable.jambu)
    );
    static  {
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් පූසා තෝරන්න.", answers_1_1, "පූසා"));
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් රෝස මල තෝරන්න.", answers_1_2, "රෝස මල"));
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් මේසය තෝරන්න.", answers_1_3, "මේසය"));
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් අහස තෝරන්න.", answers_1_4, "අහස"));
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් කුරුල්ලා තෝරන්න.", answers_1_5, "කපුටා"));
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් අඹ ගෙඩිය තෝරන්න.", answers_1_6, "අඹ ගෙඩිය"));
        quizQuestions_1.add(new QuizQuestion("පහත රූප වලින් එළවළුව තෝරන්න.", answers_1_7, "කැරට්"));
    }

    public static Quiz animalImgQuiz = new Quiz("rEm y÷kd .ekSu", R.raw.test, quizQuestions_1);

    // SET 2
    public static List<QuizQuestion> quizQuestions_2 = new ArrayList<>();
    static List<Answer> answers_2_1 = Arrays.asList(
            new Answer("බල්ලා", R.drawable.dog),
            new Answer("පූසා", R.drawable.cat),
            new Answer("අශ්වයා", R.drawable.horse),
            new Answer("හාවා", R.drawable.rabbit)
    );
    static List<Answer> answers_2_2 = Arrays.asList(
            new Answer("අශ්වයා", R.drawable.horse),
            new Answer("පූසා", R.drawable.cat),
            new Answer("බල්ලා", R.drawable.dog),
            new Answer("එළදෙන", R.drawable.cow)
    );
    static List<Answer> answers_2_3 = Arrays.asList(
            new Answer("අශ්වයා", R.drawable.horse),
            new Answer("කොටියා", R.drawable.tiger),
            new Answer("අලියා", R.drawable.elephant),
            new Answer("එළදෙන", R.drawable.cow)
    );
    static List<Answer> answers_2_4 = Arrays.asList(
            new Answer("කපුටා", R.drawable.crow),
            new Answer("එළදෙන", R.drawable.cow),
            new Answer("හාවා", R.drawable.rabbit),
            new Answer("කොටියා", R.drawable.tiger)
    );
    static List<Answer> answers_2_5 = Arrays.asList(
            new Answer("බල්ලා", R.drawable.dog),
            new Answer("හාවා", R.drawable.rabbit),
            new Answer("එළුවා", R.drawable.goat),
            new Answer("අලියා", R.drawable.elephant)
    );
    static List<Answer> answers_2_6 = Arrays.asList(
            new Answer("ගෙම්බා", R.drawable.frog),
            new Answer("අශ්වයා", R.drawable.horse),
            new Answer("කොටියා", R.drawable.tiger),
            new Answer("එළදෙන", R.drawable.cow)
    );
    static List<Answer> answers_2_7 = Arrays.asList(
            new Answer("හාවා", R.drawable.rabbit),
            new Answer("පූසා", R.drawable.cat),
            new Answer("එළදෙන", R.drawable.cow),
            new Answer("කුකුළා", R.drawable.rooster)
    );
    static  {
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_1, "බල්ලා", R.raw.dog_bark));
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_2, "පූසා", R.raw.cat_meow));
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_3, "එළදෙන", R.raw.cow_sound));
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_4, "කපුටා", R.raw.crow_sound));
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_5, "එළුවා", R.raw.goat_sound));
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_6, "ගෙම්බා", R.raw.frog_sound));
        quizQuestions_2.add(new QuizQuestion("පහත හඬ සහිත සත්වයාගේ රූපය තෝරන්න.", answers_2_7, "කුකුළා", R.raw.chicken_sound));
    }

    public static Quiz animalAudioQuiz = new Quiz("Yío y÷kd .ekSu", R.raw.test, quizQuestions_2, true);

}
