package com.example.elementaryapp2.services.recycler_view;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private final List<Answer> answerImages;
    private final OnAnswerClickListener clickListener;
    private int selectedPosition = -1;

    public interface OnAnswerClickListener {
        void onAnswerClick(Answer answer);
    }


    public AnswerAdapter(List<Answer> answerImages, OnAnswerClickListener clickListener) {
        this.answerImages = answerImages;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Answer currentAnswer = answerImages.get(position);
        holder.imageView.setImageResource(currentAnswer.resource);

        if (position == selectedPosition) {
            holder.optionBg.setCardBackgroundColor(Color.YELLOW);
        } else {
            holder.optionBg.setCardBackgroundColor(Color.TRANSPARENT);
        }

        // Set the click listener
//        holder.itemView.setOnClickListener(v -> {
//            if (clickListener != null) {
//                clickListener.onAnswerClick(currentAnswer);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return answerImages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView optionBg;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.answerImage);
            optionBg = itemView.findViewById(R.id.optionBg);

            itemView.setOnClickListener(v -> {
                int adapterPosition = getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    selectedPosition = adapterPosition;
                    notifyDataSetChanged();
                    if (clickListener != null) {
                        clickListener.onAnswerClick(answerImages.get(adapterPosition));
                    }
                }
            });
        }
    }
}

