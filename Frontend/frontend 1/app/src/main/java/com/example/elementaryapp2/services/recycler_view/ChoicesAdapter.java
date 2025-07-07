package com.example.elementaryapp2.services.recycler_view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Answer;

import java.util.List;

public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.ChoiceViewHolder> {

    private List<Answer> choices;
    private int selectedPosition = -1;

    private OnChoiceSelectedListener listener;

    public ChoicesAdapter(List<Answer> choices, OnChoiceSelectedListener listener) {
        this.choices = choices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_choice, parent, false);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder holder, int position) {
        holder.choiceText.setText(choices.get(position).answerName);
        holder.choiceText.setChecked(position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public class ChoiceViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView choiceText;

        public ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            choiceText = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(v -> {
                int adapterPosition = getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    selectedPosition = adapterPosition;
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onChoiceSelected(choices.get(adapterPosition));
                    }
                }
            });
        }
    }

    public interface OnChoiceSelectedListener {
        void onChoiceSelected(Answer answer);
    }
}

