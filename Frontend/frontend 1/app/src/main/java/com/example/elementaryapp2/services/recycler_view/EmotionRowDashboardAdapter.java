package com.example.elementaryapp2.services.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elementaryapp2.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class EmotionRowDashboardAdapter extends RecyclerView.Adapter<EmotionRowDashboardAdapter.ViewHolder> {
    private final JSONArray emotionInfo;


    public EmotionRowDashboardAdapter(JSONArray emotionInfo) {
        this.emotionInfo = emotionInfo;
    }

    @NonNull
    @Override
    public EmotionRowDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emotion_result_dashboard, parent, false);
        return new EmotionRowDashboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmotionRowDashboardAdapter.ViewHolder holder, int position) {
        try {
            JSONArray currentInfo = emotionInfo.getJSONArray(position);
            holder.emotion.setText(currentInfo.getString(0));
            holder.value.setText(currentInfo.getString(1));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return emotionInfo.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView value, emotion;

        ViewHolder(View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.value);
            emotion = itemView.findViewById(R.id.emotion);
        }
    }
}
