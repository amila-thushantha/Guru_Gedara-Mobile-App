package com.example.elementaryapp2.services.quiz;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.drag_classes.DragQuestion;
import com.example.elementaryapp2.classes.drag_classes.DragSentence;
import com.example.elementaryapp2.classes.face_activity_classes.QuizActivityQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaceActivityQuestionData {

    // greeting activity 1
    public static List<QuizActivityQuestion> greetings_1 = new ArrayList<>();
    static List<Answer> greetings_1_1 = new ArrayList<>(Arrays.asList(
            new Answer("ආයුබෝවන්", null),
            new Answer("ස්තූතී", null),
            new Answer("සුභ රාත්\u200Dරියක්", null)

    ));static List<Answer> greetings_1_2 = new ArrayList<>(Arrays.asList(
            new Answer("ගිහින් එන්නම්", null),
            new Answer("සුභ උදෑසනක්", null),
            new Answer("සමාවෙන්න", null)

    ));

    static {
        greetings_1.add(new QuizActivityQuestion("යමෙකු ඔබට උදව් කළ විට දැක්විය යුතු ගැළපෙන ප්\u200Dරතිචාරය තෝරන්න.", greetings_1_1, "ස්තූතී", "text", R.drawable.give_help_image));
        greetings_1.add(new QuizActivityQuestion("ඔබ උදෑසන අවදි වන විට දිය යුතු ගැලපෙන ප්\u200Dරතිචාරය තෝරන්න.", greetings_1_2, "සුභ උදෑසනක්", "text", R.drawable.wake_up_image));
    }

    // greeting activity 2
    public static List<QuizActivityQuestion> greetings_2 = new ArrayList<>();
    static List<Answer> greetings_2_1 = new ArrayList<>(Arrays.asList(
            new Answer("ගම", null),
            new Answer("වයස", null),
            new Answer("නම", null)

    ));

    static List<DragSentence> drag_sentences_2_1 = new ArrayList<>(Arrays.asList(
            new DragSentence("මගේ", "රවිඳු.", "නම"),
            new DragSentence("මගේ", "මාතර.", "ගම"),
            new DragSentence("මගේ", "අවුරුදු 8යි.", "වයස")
    ));

    static {
        greetings_2.add(new QuizActivityQuestion("ගැලපෙන පිළිතුර ඇද දමන්න.", new DragQuestion(drag_sentences_2_1, greetings_2_1), "drag", R.drawable.ravi_image));
    }

    // good manners activity 1
    public static List<QuizActivityQuestion> manners_1 = new ArrayList<>();
    static List<Answer> manners_1_1 = new ArrayList<>(Arrays.asList(
            new Answer("ආයුබෝවන්", null),
            new Answer("සුභ රාත්\u200Dරියක්", null),
            new Answer("සමාවෙන්න", null)


    ));static List<Answer> manners_1_2 = new ArrayList<>(Arrays.asList(
            new Answer("මම ඇතුලට එන්නද?", null),
            new Answer("කරුණාකර ඉවත් වෙන්න", null),
            new Answer("සමාවෙන්න", null)

    ));

    static {
        manners_1.add(new QuizActivityQuestion("ඔයා බඳුනක් කැඩුවා. ඔයා කොහොමද සමාව ඉල්ලන්නේ?", manners_1_1, "සමාවෙන්න", "text", R.drawable.broke_vace_image));
        manners_1.add(new QuizActivityQuestion("ඔබ කාමරයකට ඇතුළු වන විට දැක්විය යුතු ප්\u200Dරතිචාරය තෝරන්න.", manners_1_2, "මම ඇතුලට එන්නද?", "text", R.drawable.enter_door_image));
    }

    // good manners activity 2
    public static List<QuizActivityQuestion> manners_2 = new ArrayList<>();
    static List<Answer> manners_2_1 = new ArrayList<>(Arrays.asList(
            new Answer("කරුණාකර", null),
            new Answer("සමාවෙන්න", null),
            new Answer("ස්තූතියි", null)

    ));

    static List<DragSentence> drag_sentences_manners_2_1 = new ArrayList<>(Arrays.asList(
            new DragSentence("ඔයාලට බාධා ක\u200Dරාට", ".", "සමාවෙන්න"),
            new DragSentence("", "අමතර පෑනක් දෙන්න පුලුවන්ද?.", "කරුණාකර"),
            new DragSentence("පෑනක් ලබා දුන්නාට", ".", "ස්තූතියි")
    ));

    static {
        manners_2.add(new QuizActivityQuestion("ගැලපෙන පිළිතුර ඇද දමන්න.", new DragQuestion(drag_sentences_manners_2_1, manners_2_1), "drag", R.drawable.get_pen_image));
    }
}
