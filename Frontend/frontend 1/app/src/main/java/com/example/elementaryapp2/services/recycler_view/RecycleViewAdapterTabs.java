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
import com.example.elementaryapp2.classes.Tab;
import com.example.elementaryapp2.ui.DrawScreenActivity;
import com.example.elementaryapp2.ui.EmotionCaptureWelcomeActivity;
import com.example.elementaryapp2.ui.FingerCountWelcomeActivity;
import com.example.elementaryapp2.ui.QuizWelcomeActivity;
import com.example.elementaryapp2.ui.ShapeActivity;
import com.example.elementaryapp2.ui.SinhalaAlphabetActivity;
import com.example.elementaryapp2.ui.SubPartActivity;

import java.util.ArrayList;

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterTabs extends RecyclerView.Adapter<ViewHolderTabs>{

    Context context;
    ArrayList<Tab> list;

    int selectedPosition = 0;
    RecyclerView recyclerView;


    //constructor
    public RecycleViewAdapterTabs(Context context, ArrayList<Tab> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderTabs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tab_item, parent, false);
        return new ViewHolderTabs(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTabs holder, int position) {
        Tab tab = list.get(position);
        holder.pageType = tab.pageType;
        holder.subtype = tab.pageSubtype;
        holder.quiz = tab.quiz;

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
class ViewHolderTabs extends RecyclerView.ViewHolder{

    TextView header, subText;

    ImageButton imageButton;

    CardView cardView, btnBg;

    String pageType;
    ImageView bgImg;
    int subtype;
    Quiz quiz;

    private RecycleViewAdapterTabs adapter;

    public ViewHolderTabs(@NonNull View itemView) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
        subText = itemView.findViewById(R.id.subText);
        imageButton = itemView.findViewById(R.id.imageButton);
        cardView = itemView.findViewById(R.id.card_bg);
        btnBg = itemView.findViewById(R.id.btnBg);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageType.equals("draw")) {
                    Intent intent = new Intent(adapter.context, DrawScreenActivity.class);
                    intent.putExtra("subtype", subtype);
                    adapter.context.startActivity(intent);
                }
                else if (pageType.equals("quiz")) {
                    Intent intent = new Intent(adapter.context, QuizWelcomeActivity.class);
                    intent.putExtra("quizData", quiz);
                    adapter.context.startActivity(intent);
                }
                else if (pageType.equals("count")) {
                    Intent intent = new Intent(adapter.context, FingerCountWelcomeActivity.class);
                    adapter.context.startActivity(intent);
                } else if (pageType.equals("emotion")) {
                    Intent intent = new Intent(adapter.context, EmotionCaptureWelcomeActivity.class);
                    adapter.context.startActivity(intent);
                } else if (pageType.equals("math_game")) {
                    Intent intent = new Intent(adapter.context, ShapeActivity.class);
                    adapter.context.startActivity(intent);
                }else if (pageType.equals("math_lesson")) {
                    Intent intent = new Intent(adapter.context, SubPartActivity.class);
                    intent.putExtra("subPageType", 0);
                    adapter.context.startActivity(intent);
                } else if (pageType.equals("sinhala_game")) {
                    Intent intent = new Intent(adapter.context, SinhalaAlphabetActivity.class);
                    adapter.context.startActivity(intent);
                } else if (pageType.equals("communication_lesson")) {
                    Intent intent = new Intent(adapter.context, SubPartActivity.class);
                    intent.putExtra("subPageType", 1);
                    adapter.context.startActivity(intent);
                } else if (pageType.equals("day_to_day_lesson")) {
                    Intent intent = new Intent(adapter.context, SubPartActivity.class);
                    intent.putExtra("subPageType", 2);
                    adapter.context.startActivity(intent);
                }
            }
        });
    }

    //linking the adapter
    public ViewHolderTabs linkAdapter (RecycleViewAdapterTabs adapter) {
        this.adapter = adapter;
        return this;
    }
}
