package com.example.elementaryapp2.services;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.elementaryapp2.R;
import com.example.elementaryapp2.services.recycler_view.AnswerAdapter;
import com.example.elementaryapp2.services.recycler_view.EmotionRowAdapter;

import org.json.JSONArray;

import java.util.List;

public class AlertDialogUtil {
    private static AlertDialog alertDialogLoading, alertDialogQuiz;

    public static void showLoadingDialog(Context context, String header) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentAlertDialog);
        View customLayout = LayoutInflater.from(context).inflate(R.layout.loading_dialog_layout, null);
        builder.setView(customLayout);
        TextView headerTextView = customLayout.findViewById(R.id.loading_status);
        ImageView imageView = customLayout.findViewById(R.id.gifImageView);

        Glide.with(context)
                .asGif()
                .load(R.drawable.loading_gif)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);

        headerTextView.setText(header);
        builder.setCancelable(false);
        alertDialogLoading = builder.create();
        alertDialogLoading.show();
    }

    public static void dismissLoadingDialog() {
        if (alertDialogLoading != null && alertDialogLoading.isShowing()) {
            alertDialogLoading.dismiss();
        }
    }

    public static void ShowQuizAlertDialog(Context context, int bgClr, int type, boolean btnVisible, OnButtonClickListener listener) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null);
        builder.setView(customLayout);

        // Set up your custom views and actions
        TextView Header = customLayout.findViewById(R.id.heading);
        TextView subText = customLayout.findViewById(R.id.confidence);
        ImageView imageView = customLayout.findViewById(R.id.dialog_image);
        Button btn = customLayout.findViewById(R.id.button);

        if (btnVisible) {
            btn.setVisibility(View.VISIBLE);
            builder.setCancelable(true);
        } else {
            btn.setVisibility(View.GONE);
            subText.setVisibility(View.GONE);
            builder.setCancelable(false);
        }

        if (type == 0 || type == 2) {
            Header.setText("ksjerÈhsæ"); //correct
            subText.setText("wmf.a iqN me;=ïæ");
//            imageView.setImageResource(R.drawable.correct_animal_image);
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.celebrate_gif)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageView);
            if (type == 0) {
                btn.setText("B<Õ"); //next
            } else {
                btn.setText("wjika lrkak"); //finish
            }
        } else if (type == 1) {
            Header.setText("jerÈhsæ"); //incorrect
            subText.setText("kej; jrla W;aidy lrkak");
//            imageView.setImageResource(R.drawable.incorrect_animal_image);
            Glide.with(context)
                    .asGif()
                    .load(R.drawable.question_gif)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageView);
            btn.setText("W;aidy lrkak");
        }

        btn.setBackgroundColor(bgClr);

        // Create and show the dialog
        alertDialogQuiz = builder.create();
        alertDialogQuiz.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener
                if (listener != null) {
                    listener.onButtonClicked(true);
                }
                // Dismiss the dialog
                alertDialogQuiz.dismiss();
            }
        });
    }

    public static void dismissQuizDialog() {
        if (alertDialogQuiz != null && alertDialogQuiz.isShowing()) {
            alertDialogQuiz.dismiss();
        }
    }

    // Callback Interface
    public interface OnButtonClickListener {
        void onButtonClicked(boolean isClicked);
    }

    public static void ShowFingerCountingAlertDialog(Context context, String score, String error, int bgClr, OnButtonClickListener listener) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = LayoutInflater.from(context).inflate(R.layout.finger_counting_alert_dialog_layout, null);
        builder.setView(customLayout);

        TextView scoreText = customLayout.findViewById(R.id.score);
        TextView subText = customLayout.findViewById(R.id.confidence);
        Button btn = customLayout.findViewById(R.id.button);
        ImageView imageView = customLayout.findViewById(R.id.error_image);

        // Set up your custom views and actions
        if (error.equals("0")) {
            scoreText.setText(score + "%");
            scoreText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        } else {
            scoreText.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            subText.setText("w;a wkdjrKh fkdùh"); //No hands detected
        }


        btn.setBackgroundColor(bgClr);

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener
                if (listener != null) {
                    listener.onButtonClicked(true);
                }
                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });
    }

    public static void ShowEmotionResultAlertDialog(Context context, String score, JSONArray emotionInfo, String error, int bgClr, OnButtonClickListener listener) {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentAlertDialog);

        // Inflate the custom layout
        View customLayout = LayoutInflater.from(context).inflate(R.layout.emotion_activity_dialog_layout, null);
        builder.setView(customLayout);

        TextView scoreText = customLayout.findViewById(R.id.score);
        LinearLayout errorSection = customLayout.findViewById(R.id.errorSection);
        RecyclerView emotionRecyclerView = customLayout.findViewById(R.id.emotionRecyclerView);
        Button btn = customLayout.findViewById(R.id.button);

        scoreText.setText(score);

        // Set up your custom views and actions
        if (error.equals("0")) {
            emotionRecyclerView.setVisibility(View.VISIBLE);
            errorSection.setVisibility(View.GONE);

            // Set the adapter
            EmotionRowAdapter adapter = new EmotionRowAdapter(emotionInfo);
            emotionRecyclerView.setAdapter(adapter);

            // Set up layout
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            emotionRecyclerView.setLayoutManager(layoutManager);

        } else {
            emotionRecyclerView.setVisibility(View.GONE);
            errorSection.setVisibility(View.VISIBLE);
        }


        btn.setBackgroundColor(bgClr);

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener
                if (listener != null) {
                    listener.onButtonClicked(true);
                }
                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });
    }

}

