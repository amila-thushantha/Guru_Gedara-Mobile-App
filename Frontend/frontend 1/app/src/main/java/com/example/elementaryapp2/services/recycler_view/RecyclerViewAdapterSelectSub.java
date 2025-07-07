package com.example.elementaryapp2.services.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.SubTab;
import com.example.elementaryapp2.classes.Tab;
import com.example.elementaryapp2.classes.TuteMathQuestion;
import com.example.elementaryapp2.classes.face_activity_classes.QuizActivityQuestion;
import com.example.elementaryapp2.ui.EmotionCaptureWelcomeActivity;
import com.example.elementaryapp2.ui.QuizWelcomeActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterSelectSub extends RecyclerView.Adapter<ViewHolderSelectSub>{

    Context context;
    ArrayList<SubTab> list;

    int selectedPosition = 0;
    RecyclerView recyclerView;


    //constructor
    public RecyclerViewAdapterSelectSub(Context context, ArrayList<SubTab> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderSelectSub onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_tab_item, parent, false);
        return new ViewHolderSelectSub(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSelectSub holder, int position) {
        SubTab tab = list.get(position);
        holder.pageType = tab.pageType;
        holder.subtype = tab.pageSubtype;
        holder.quiz = tab.faceActivityProblems;
        holder.tutorialResource = tab.tutorialResource;

        holder.header.setText(tab.header);
        holder.subText.setText(tab.subText);
        holder.imageButton.setImageResource(tab.image);
        holder.btnBg.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),tab.btnBgClr));
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),tab.bgClr));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Tab getIntentData() {
        if (selectedPosition != -1) {
            return list.get(selectedPosition);
        } else {
            return null;
        }
    }
}

//viewholder for all the item elements and functions
class ViewHolderSelectSub extends RecyclerView.ViewHolder{

    TextView header, subText;

    ImageButton imageButton;

    CardView cardView, btnBg;

    String pageType;
    ImageView bgImg;
    int subtype;
    List<QuizActivityQuestion> quiz;
    Integer tutorialResource;

    private RecyclerViewAdapterSelectSub adapter;

    public ViewHolderSelectSub(@NonNull View itemView) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
        subText = itemView.findViewById(R.id.subText);
        imageButton = itemView.findViewById(R.id.imageButton);
        cardView = itemView.findViewById(R.id.card_bg);
        btnBg = itemView.findViewById(R.id.btnBg);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adapter.context, EmotionCaptureWelcomeActivity.class);
                intent.putExtra("quizData", (Serializable) quiz);
                adapter.context.startActivity(intent);
            }
        });
    }

    //linking the adapter
    public ViewHolderSelectSub linkAdapter (RecyclerViewAdapterSelectSub adapter) {
        this.adapter = adapter;
        return this;
    }
}
