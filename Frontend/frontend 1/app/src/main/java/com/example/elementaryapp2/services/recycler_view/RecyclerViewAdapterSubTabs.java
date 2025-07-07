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
import com.example.elementaryapp2.classes.Quiz;
import com.example.elementaryapp2.classes.SubTab;
import com.example.elementaryapp2.classes.Tab;
import com.example.elementaryapp2.classes.TuteMathQuestion;
import com.example.elementaryapp2.ui.DrawScreenActivity;
import com.example.elementaryapp2.ui.EmotionCaptureActivity;
import com.example.elementaryapp2.ui.EmotionCaptureWelcomeActivity;
import com.example.elementaryapp2.ui.FingerCountActivity;
import com.example.elementaryapp2.ui.FingerCountWelcomeActivity;
import com.example.elementaryapp2.ui.MathTuteActivity;
import com.example.elementaryapp2.ui.QuizWelcomeActivity;
import com.example.elementaryapp2.ui.SelectSubActivity;
import com.example.elementaryapp2.ui.ShapeActivity;
import com.example.elementaryapp2.ui.SinhalaAlphabetActivity;
import com.example.elementaryapp2.ui.TutorialScreenActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Adapting recycler view
//get data from main activity using a constructer
public class RecyclerViewAdapterSubTabs extends RecyclerView.Adapter<ViewHolderSubTabs>{

    Context context;
    ArrayList<SubTab> list;

    int selectedPosition = 0;
    RecyclerView recyclerView;


    //constructor
    public RecyclerViewAdapterSubTabs(Context context, ArrayList<SubTab> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderSubTabs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_tab_item, parent, false);
        return new ViewHolderSubTabs(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSubTabs holder, int position) {
        SubTab tab = list.get(position);
        holder.pageType = tab.pageType;
        holder.subtype = tab.pageSubtype;
        holder.quiz = tab.problems;
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
class ViewHolderSubTabs extends RecyclerView.ViewHolder{

    TextView header, subText;

    ImageButton imageButton;

    CardView cardView, btnBg;

    String pageType;
    ImageView bgImg;
    int subtype;
    List<TuteMathQuestion> quiz;
    Integer tutorialResource;

    private RecyclerViewAdapterSubTabs adapter;

    public ViewHolderSubTabs(@NonNull View itemView) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
        subText = itemView.findViewById(R.id.subText);
        imageButton = itemView.findViewById(R.id.imageButton);
        cardView = itemView.findViewById(R.id.card_bg);
        btnBg = itemView.findViewById(R.id.btnBg);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageType.equals("math")) {
                    Intent intent = new Intent(adapter.context, QuizWelcomeActivity.class);
                    intent.putExtra("quizData", (Serializable) quiz);
                    intent.putExtra("tutorialResource", tutorialResource);
                    intent.putExtra("type", 1);
                    adapter.context.startActivity(intent);
                } else if (pageType.equals("communication")) {
                    Intent intent = new Intent(adapter.context, QuizWelcomeActivity.class);
                    intent.putExtra("tutorialResource", tutorialResource);
                    intent.putExtra("type", 2);
                    intent.putExtra("subtype", subtype);
                    adapter.context.startActivity(intent);
                }
            }
        });
    }

    //linking the adapter
    public ViewHolderSubTabs linkAdapter (RecyclerViewAdapterSubTabs adapter) {
        this.adapter = adapter;
        return this;
    }
}

