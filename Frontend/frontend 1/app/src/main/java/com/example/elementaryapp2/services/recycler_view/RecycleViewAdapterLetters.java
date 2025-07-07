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

//Adapting recycler view
//get data from main activity using a constructer
public class RecycleViewAdapterLetters extends RecyclerView.Adapter<ViewHolderLetters>{

    Context context;
    ArrayList<Letter> list;
    Boolean isSinhala;

    int selectedPosition = 0;
    int bgClr;
    RecyclerView recyclerView;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }


    //constructor
    public RecycleViewAdapterLetters(Context context, ArrayList<Letter> list, RecyclerView recyclerView, int bgClr, Boolean isSinhala) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.bgClr = bgClr;
        this.isSinhala = isSinhala;
    }

    @NonNull
    @Override
    public ViewHolderLetters onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.letter_item, parent, false);
        return new ViewHolderLetters(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLetters holder, int position) {
        Letter letter = list.get(position);


        if (isSinhala) {
            holder.number.setVisibility(View.GONE);
            holder.sinhala.setText(letter.letter);
        } else {
            holder.sinhala.setVisibility(View.GONE);
            holder.number.setText(letter.letter);
        }

        // Set background color based on the selected position
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), bgClr)); // Change the color as needed
            holder.number.setTextColor(Color.WHITE);
            holder.sinhala.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE); // Reset to default
            holder.number.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Heading));
            holder.sinhala.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Heading));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

//viewholder for all the item elements and functions
class ViewHolderLetters extends RecyclerView.ViewHolder{

    TextView number, sinhala;
    CardView cardView;

    private RecycleViewAdapterLetters adapter;

    public ViewHolderLetters(@NonNull View itemView) {
        super(itemView);

        number = itemView.findViewById(R.id.number);
        sinhala = itemView.findViewById(R.id.sinhala);
        cardView = itemView.findViewById(R.id.card_bg);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectedPosition = getAdapterPosition();

                if (adapter.itemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.itemClickListener.onItemClick(position);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (adapter.itemClickListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        adapter.itemClickListener.onItemClick(position);
//                    }
//                }
//            }
//        });

    }

    //linking the adapter
    public ViewHolderLetters linkAdapter (RecycleViewAdapterLetters adapter) {
        this.adapter = adapter;
        return this;
    }
}
