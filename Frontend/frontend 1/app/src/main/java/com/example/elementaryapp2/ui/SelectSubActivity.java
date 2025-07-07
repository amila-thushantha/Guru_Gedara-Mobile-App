package com.example.elementaryapp2.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.SubTab;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.quiz.FaceActivityQuestionData;
import com.example.elementaryapp2.services.quiz.MathTuteQuizData;
import com.example.elementaryapp2.services.recycler_view.RecyclerViewAdapterSelectSub;
import com.example.elementaryapp2.services.recycler_view.RecyclerViewAdapterSubTabs;

import java.util.ArrayList;

public class SelectSubActivity extends AppCompatActivity {

    private TextView tvSubPageName;
    private RecyclerView recyclerView;
    private ArrayList<SubTab> list1;
    private RecyclerViewAdapterSelectSub adapter;
    private int subPageType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub);

        Services.onPressBack(this);

        subPageType = getIntent().getIntExtra("subActivityType", 0);

        tvSubPageName = findViewById(R.id.subPageName);
        recyclerView = findViewById(R.id.recyclerView);

        if (subPageType == 0) {
            list1 = new ArrayList<>();
            list1.add(new SubTab(R.drawable.animal_12, "ක්\u200Dරියාකාරකම් 1", "ගැලපෙන ආචාර", "greet", 0, R.color.bgClr_2, R.color.bgClr_2_dark, FaceActivityQuestionData.greetings_1));
            list1.add(new SubTab(R.drawable.animal_13, "ක්\u200Dරියාකාරකම් 2", "ගැලපෙන ප්\u200Dරතිචාර", "greet", 1, R.color.bgClr_2, R.color.bgClr_2_dark, FaceActivityQuestionData.greetings_2));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            layoutManager1.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(layoutManager1);
            adapter = new RecyclerViewAdapterSelectSub(this, list1, recyclerView);
            recyclerView.setAdapter(adapter);
        } else if (subPageType == 1) {
            list1 = new ArrayList<>();
            list1.add(new SubTab(R.drawable.animal_12, "ක්\u200Dරියාකාරකම් 1", "ගැලපෙන ආචාර", "greet", 0, R.color.bgClr_2, R.color.bgClr_2_dark, FaceActivityQuestionData.manners_1));
            list1.add(new SubTab(R.drawable.animal_13, "ක්\u200Dරියාකාරකම් 2", "ගැලපෙන ප්\u200Dරතිචාර", "greet", 1, R.color.bgClr_2, R.color.bgClr_2_dark, FaceActivityQuestionData.manners_2));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            layoutManager1.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(layoutManager1);
            adapter = new RecyclerViewAdapterSelectSub(this, list1, recyclerView);
            recyclerView.setAdapter(adapter);
        }

    }
}