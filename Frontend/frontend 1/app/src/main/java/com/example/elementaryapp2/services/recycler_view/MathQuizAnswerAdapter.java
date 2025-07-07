package com.example.elementaryapp2.services.recycler_view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;
import com.example.elementaryapp2.classes.ShapeBox;
import com.example.elementaryapp2.services.shape.ShapeGenerator;

import java.util.List;

public class MathQuizAnswerAdapter extends RecyclerView.Adapter<MathQuizAnswerAdapter.ViewHolder> {

    private final List<ShapeBox> answers;
    private final OnAnswerClickListener clickListener;
    private final Context context;

    public interface OnAnswerClickListener {
        void onAnswerClick(ShapeBox answer);
    }


    public MathQuizAnswerAdapter(List<ShapeBox> answers, OnAnswerClickListener clickListener, Context context) {
        this.answers = answers;
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_math_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShapeBox currentAnswer = answers.get(position);
        ShapeGenerator shapeGenerator = new ShapeGenerator(context, holder.choice);
        shapeGenerator.generateShapes(currentAnswer.shape, currentAnswer.shapeColor, currentAnswer.count);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onAnswerClick(currentAnswer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout choice;

        ViewHolder(View itemView) {
            super(itemView);
            choice = itemView.findViewById(R.id.choice);
        }
    }
}

