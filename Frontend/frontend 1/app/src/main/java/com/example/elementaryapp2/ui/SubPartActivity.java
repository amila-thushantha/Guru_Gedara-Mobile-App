package com.example.elementaryapp2.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.SubTab;
import com.example.elementaryapp2.classes.Tab;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.quiz.MathTuteQuizData;
import com.example.elementaryapp2.services.recycler_view.RecycleViewAdapterTabs;
import com.example.elementaryapp2.services.recycler_view.RecyclerViewAdapterSubTabs;

import java.util.ArrayList;
import java.util.List;

public class SubPartActivity extends AppCompatActivity {

    private TextView tvSubPageName;
    private RecyclerView recyclerView;
    private ArrayList<SubTab> list1;
    private RecyclerViewAdapterSubTabs adapter;
    private int subPageType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_part);

        Services.onPressBack(this);

        subPageType = getIntent().getIntExtra("subPageType", 0);

        tvSubPageName = findViewById(R.id.subPageName);
        recyclerView = findViewById(R.id.recyclerView);

        if (subPageType == 0) {
            tvSubPageName.setText("iq¿ lsÍï bf.k .uq");

            list1 = new ArrayList<>();
            list1.add(new SubTab(R.drawable.add_subtract, "පළමු පාඩම", "සුළු කිරීම්", "math", 0, R.color.bgClr_1, R.color.bgClr_1_dark, MathTuteQuizData.mathTuteQuiz1Data, R.raw.test));
            list1.add(new SubTab(R.drawable.multiply_divide, "දෙවන පාඩම", "සුළු කිරීම්", "math", 1, R.color.bgClr_1, R.color.bgClr_1_dark, MathTuteQuizData.mathTuteQuiz2Data, R.raw.test));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            layoutManager1.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(layoutManager1);
            adapter = new RecyclerViewAdapterSubTabs(this, list1, recyclerView);
            recyclerView.setAdapter(adapter);
        } else if (subPageType == 1) { // communication
            tvSubPageName.setText("ikaksfõok l=i,;d bf.k .uq");

            list1 = new ArrayList<>();
            list1.add(new SubTab(R.drawable.animal_10, "පළමු පාඩම", "කතා කරන විදිය", "communication", 0, R.color.bgClr_2, R.color.bgClr_2_dark, null, R.raw.test));
            list1.add(new SubTab(R.drawable.animal_11, "දෙවන පාඩම", "හොඳ පුරුදු", "communication", 1, R.color.bgClr_2, R.color.bgClr_2_dark, null, R.raw.test));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            layoutManager1.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(layoutManager1);
            adapter = new RecyclerViewAdapterSubTabs(this, list1, recyclerView);
            recyclerView.setAdapter(adapter);
        } else if (subPageType == 2) { // dat_to_day activities
            tvSubPageName.setText("tÈfkod foaj,a bf.k .uq");

            list1 = new ArrayList<>();
            list1.add(new SubTab(R.drawable.add_subtract, "පළමු පාඩම", "කතා කරන විදිය", "dat_to_day", 2, R.color.bgClr_2, R.color.bgClr_2_dark, null, R.raw.test));
            list1.add(new SubTab(R.drawable.add_subtract, "දෙවන පාඩම", "හොඳ පුරුදු", "dat_to_day", 3, R.color.bgClr_2, R.color.bgClr_2_dark, null, R.raw.test));

            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
            layoutManager1.setOrientation(RecyclerView.VERTICAL);

            recyclerView.setLayoutManager(layoutManager1);
            adapter = new RecyclerViewAdapterSubTabs(this, list1, recyclerView);
            recyclerView.setAdapter(adapter);
        }

    }
}