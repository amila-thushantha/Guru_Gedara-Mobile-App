package com.example.elementaryapp2.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Tab;
import com.example.elementaryapp2.database.DatabaseHelper;
import com.example.elementaryapp2.services.quiz.QuizData;
import com.example.elementaryapp2.services.recycler_view.RecycleViewAdapterTabs;
import com.example.elementaryapp2.ui.components.DashboardFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {

    ImageView pfp;
    DatabaseHelper databaseHelper;
    TextView username, dateText;
    String email = null;
    Date date;
    RecycleViewAdapterTabs adapter1, adapter2, adapter3;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    ArrayList<Tab> list1, list2, list3;
    Button goToTestBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Load DashboardFragment dynamically
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.dashboard_container, new DashboardFragment());
        transaction.commit();

        username = v.findViewById(R.id.username);
        pfp = v.findViewById(R.id.pfp);
        goToTestBtn = v.findViewById(R.id.testButton);

        goToTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TestWelcomeActivity.class);
                startActivity(intent);
            }
        });
//        dateText = v.findViewById(R.id.date);
//
//        // Get the current date
//        date = new Date();
//
//        // Format the date to "JUNE 06"
//        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
//        String formattedDate = sdf.format(date);
//
//        // Capitalize the month part
//        formattedDate = formattedDate.toUpperCase(Locale.ENGLISH);
//
//        dateText.setText(formattedDate);


        databaseHelper = new DatabaseHelper(this.getContext());
        Cursor cursor = databaseHelper.getAllData();
        while (cursor.moveToNext()) {
            email = cursor.getString(0);
            String filePath = cursor.getString(2);
            if (!Objects.equals(filePath, "")) {
                Uri fileUri = getUriFromFilePath(getContext(), filePath);
                pfp.setImageURI(fileUri);
            } else {
                pfp.setImageResource(R.drawable.default_pfp);
            }
            username.setText(cursor.getString(1));
        }

        // maths
        list1 = new ArrayList<>();
        list1.add(new Tab(R.drawable.animal_4, "අංක ලියමු", "ලිවීමේ ක්\u200Dරියාකාරකම්", "draw", 0, R.color.bgClr_1, R.color.bgClr_1_dark, null));
        list1.add(new Tab(R.drawable.animal_2, "ගණන් කරමු", "කැමරා ක්\u200Dරියාකාරකම්", "count", 0, R.color.bgClr_1, R.color.bgClr_1_dark, null));
        list1.add(new Tab(R.drawable.animal_7, "ගණිත පාඩම්", "සුළු කිරීම්", "math_lesson", 0, R.color.bgClr_1, R.color.bgClr_1_dark, null));
        list1.add(new Tab(R.drawable.correct_animal_image, "ගැටළු විසඳමු", "විනෝද ක්\u200Dරියාකාරකම්", "math_game", 0, R.color.bgClr_1, R.color.bgClr_1_dark, null));

        // sinhala
        list2 = new ArrayList<>();
        list2.add(new Tab(R.drawable.animal_3, "අකුරු ලියමු", "ලිවීමේ ක්\u200Dරියාකාරකම්", "draw", 1, R.color.bgClr_3, R.color.bgClr_3_dark, null));
        list2.add(new Tab(R.drawable.animal_1, "රූප හඳුනමු", "දෘශ්\u200Dය ක්\u200Dරියාකාරකම්", "quiz", 0, R.color.bgClr_3, R.color.bgClr_3_dark, QuizData.animalImgQuiz));
        list2.add(new Tab(R.drawable.animal_5, "ශබ්ද හඳුනමු", "ශ්\u200Dරවණ ක්\u200Dරියාකාරකම්", "quiz", 0, R.color.bgClr_3, R.color.bgClr_3_dark, QuizData.animalAudioQuiz));
        list2.add(new Tab(R.drawable.animal_6, "වචන හදමු", "විනෝද ක්\u200Dරියාකාරකම්", "sinhala_game", 0, R.color.bgClr_3, R.color.bgClr_3_dark, null));

        // other
        list3 = new ArrayList<>();
        list3.add(new Tab(R.drawable.animal_8, "සන්නිවේදනය", "පාඩම් ක්\u200Dරියාකාරකම්", "communication_lesson", 0, R.color.bgClr_2, R.color.bgClr_2_dark, null));
        list3.add(new Tab(R.drawable.animal_9, "එදිනෙදා දේවල්", "පාඩම් ක්\u200Dරියාකාරකම්", "day_to_day_lesson", 0, R.color.bgClr_2, R.color.bgClr_2_dark, null));


        recyclerView1 = v.findViewById(R.id.recyclerView1);
        recyclerView2 = v.findViewById(R.id.recyclerView2);
        recyclerView3 = v.findViewById(R.id.recyclerView3);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView1.setLayoutManager(layoutManager1);
        adapter1 = new RecycleViewAdapterTabs(getContext(), list1, recyclerView1);
        recyclerView1.setAdapter(adapter1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new RecycleViewAdapterTabs(getContext(), list2, recyclerView2);
        recyclerView2.setAdapter(adapter2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView3.setLayoutManager(layoutManager3);
        adapter3 = new RecycleViewAdapterTabs(getContext(), list3, recyclerView3);
        recyclerView3.setAdapter(adapter3);

        return v;
    }

    public static Uri getUriFromFilePath(Context context, String filePath) {
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) {
//            throw new IllegalArgumentException("File does not exist: " + filePath);
            Toast.makeText(context, "File does not exist: " + filePath, Toast.LENGTH_SHORT).show();
        }

        // Return Uri for the file using FileProvider
        return FileProvider.getUriForFile(context, "com.example.elementaryapp2.fileprovider", file);
    }
}