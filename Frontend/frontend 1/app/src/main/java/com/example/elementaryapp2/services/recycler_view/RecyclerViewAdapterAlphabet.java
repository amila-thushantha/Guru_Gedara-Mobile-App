package com.example.elementaryapp2.services.recycler_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.classes.Letter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//Adapting recycler view
//get data from main activity using a constructer
public class RecyclerViewAdapterAlphabet extends RecyclerView.Adapter<ViewHolderAlphabet> {

    Context context;
    ArrayList<Letter> list;
    int bgClr;
    RecyclerView recyclerView;

    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(String selectedString);
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public RecyclerViewAdapterAlphabet(Context context, ArrayList<Letter> list, RecyclerView recyclerView, int bgClr) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.bgClr = bgClr;
    }

    @NonNull
    @Override
    public ViewHolderAlphabet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_letter_item, parent, false);
        return new ViewHolderAlphabet(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAlphabet holder, int position) {
        Letter letter = list.get(position);
//        holder.number.setVisibility(View.GONE);
        holder.sinhala.setText(letter.sinhala);

        // Apply selected style if the item is in selectedPositions
        if (selectedPositions.contains(position)) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), bgClr));
//            holder.number.setTextColor(Color.WHITE);
            holder.sinhala.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
//            holder.number.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Heading));
            holder.sinhala.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Heading));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void toggleSelection(int position) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(Integer.valueOf(position)); // Remove if already selected
        } else {
            selectedPositions.add(position); // Add to keep order
        }
        notifyItemChanged(position);

        // Generate the selected string in order
        StringBuilder selectedString = new StringBuilder();
        for (int pos : selectedPositions) {
            selectedString.append(list.get(pos).sinhala); // Append selected letters
        }

        // Notify listener with the generated string
        if (itemClickListener != null) {
            itemClickListener.onItemClick(selectedString.toString());
        }
    }
    // Method to clear selections
    public void clearSelection() {
        selectedPositions.clear();
        notifyDataSetChanged(); // Refresh all items

        // Notify listener with empty string
        if (itemClickListener != null) {
            itemClickListener.onItemClick("");
        }
    }

}

// ViewHolder
class ViewHolderAlphabet extends RecyclerView.ViewHolder {

    TextView number, sinhala;
    CardView cardView;
    private RecyclerViewAdapterAlphabet adapter;

    public ViewHolderAlphabet(@NonNull View itemView) {
        super(itemView);

//        number = itemView.findViewById(R.id.number);
        sinhala = itemView.findViewById(R.id.sinhala);
        cardView = itemView.findViewById(R.id.card_bg);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    adapter.toggleSelection(position);
                }
            }
        });
    }

    public ViewHolderAlphabet linkAdapter(RecyclerViewAdapterAlphabet adapter) {
        this.adapter = adapter;
        return this;
    }
}

