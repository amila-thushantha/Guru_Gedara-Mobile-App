package com.example.elementaryapp2.services.quiz;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.MathEquation;
import com.example.elementaryapp2.classes.test_question_classes.MainQuestion;
import com.example.elementaryapp2.classes.test_question_classes.SubQuestion;
import com.example.elementaryapp2.classes.test_question_classes.TestLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestLevelQuestionData {

    public static List<TestLevel> testLevelQuestionData = new ArrayList<>();

    // Level 1
    public static List<MainQuestion> mainQuestion_1 = new ArrayList<>();
    // Set 1
    public static List<SubQuestion> subQuestion_1_1 = new ArrayList<>();

    static List<Answer> answers_1_1_1 = new ArrayList<>(Arrays.asList(
            new Answer("2යි", null),
            new Answer("1යි", null),
            new Answer("4යි", null)
    ));
    static SubQuestion subQuestion_1_1_1 = new SubQuestion("maths", "කිරිල්ලී මාදම් ගස් කීයක් දුටුවාද?", "1යි", "normal", answers_1_1_1, "text", null);

    static List<Answer> answers_1_1_2 = new ArrayList<>(Arrays.asList(
            new Answer("1", R.drawable.elephant),
            new Answer("2", R.drawable.sun),
            new Answer("3", R.drawable.sunflower)
    ));
    static SubQuestion subQuestion_1_1_2 = new SubQuestion("maths","දම් පාට මාදම් ගෙඩිය තෝරන්න.", "1", "normal", answers_1_1_2, "image", null);

    static {
        subQuestion_1_1.add(subQuestion_1_1_1);
        subQuestion_1_1.add(subQuestion_1_1_2);
    }
    // Set 2
    public static List<SubQuestion> subQuestion_1_2 = new ArrayList<>();

    static List<Answer> answers_1_2_1 = new ArrayList<>(Arrays.asList(
            new Answer("1", R.drawable.elephant),
            new Answer("2", R.drawable.sun)
    ));
    static SubQuestion subQuestion_1_2_1 = new SubQuestion("sinhala","සතුටින් සිටින් කුරුල්ලා තෝරන්න.", "2", "normal", answers_1_2_1, "image", null);

    static SubQuestion subQuestion_1_2_2 = new SubQuestion("maths","පහත ඇති මුලු අතු ගණන කොපමණද?", "5", "normal", null, "calc", new MathEquation(3, "+", 2, 5));

    static {
        subQuestion_1_2.add(subQuestion_1_2_1);
        subQuestion_1_2.add(subQuestion_1_2_2);
    }

    static {
        mainQuestion_1.add(new MainQuestion("එක දවසක් කකොණ්ඩ කිරිල්ලක් කූඩුවක් හදන්කන් කකොකේ ද කියො  කල්පනො කළො. කසොයො \n" +
                "කෙන, කසොයො කෙන යන විට හරි අපූරු මො දං ෙහක් දැක්කො.", subQuestion_1_1));
        mainQuestion_1.add(new MainQuestion("කැලය පුරො ඉගිකලමින් කකෝටුව, කකෝටුව බැගින් කඩොකෙන ආවො. ඒවො මො දං ෙකේ දුකන් අත්කෙක එකිකන ෙබො කූඩුවක් ෙැනුවො. ඊට පසු පුලුන් කෙනැවිත් කූඩුවට දමො සනීප.\n" +
                "කමට්ටයක් හැදුවො.", subQuestion_1_2));

    }

    // Level 2
    public static List<MainQuestion> mainQuestion_2 = new ArrayList<>();

    static {
        mainQuestion_2.add(new MainQuestion("එක දවසක් කකොණ්ඩ කිරිල්ලක් කූඩුවක් හදන්කන් කකොකේ ද කියො  කල්පනො කළො. කසොයො \n" +
                "කෙන, කසොයො කෙන යන විට හරි අපූරු මො දං ෙහක් දැක්කො.", subQuestion_1_1));
        mainQuestion_2.add(new MainQuestion("කැලය පුරො ඉගිකලමින් කකෝටුව, කකෝටුව බැගින් කඩොකෙන ආවො. ඒවො මො දං ෙකේ දුකන් අත්කෙක එකිකන ෙබො කූඩුවක් ෙැනුවො. ඊට පසු පුලුන් කෙනැවිත් කූඩුවට දමො සනීප.\n" +
                "කමට්ටයක් හැදුවො.", subQuestion_1_2));

    }

    static {
        testLevelQuestionData.add(new TestLevel("පළමු අදියර", mainQuestion_1));
        testLevelQuestionData.add(new TestLevel("දෙවන අදියර", mainQuestion_2));
    }
}
