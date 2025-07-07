package com.example.elementaryapp2.ui.components;

import static android.view.View.GONE;
import static com.example.elementaryapp2.services.Services.ipAddress;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.elementaryapp2.R;
import com.example.elementaryapp2.services.CameraFragment.CameraResponseListener;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.recycler_view.EmotionRowAdapter;
import com.example.elementaryapp2.services.recycler_view.EmotionRowDashboardAdapter;
import com.example.elementaryapp2.ui.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DashboardFragment extends Fragment {
    private TextView tvWeekness, tvEmotionError, tvWeaknessError, tvWeakSubjectOldMarks, tvWeakSubjectNewMarks;
    private ImageView dashboardImage;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dashboard_layout, container, false);

        tvWeekness = v.findViewById(R.id.weakness);
        tvEmotionError = v.findViewById(R.id.emotion_error);
        tvWeaknessError = v.findViewById(R.id.error_weakness);
        tvWeakSubjectOldMarks = v.findViewById(R.id.weakSubjectOldMarks);
        tvWeakSubjectNewMarks = v.findViewById(R.id.weakSubjectNewMarks);
        dashboardImage = v.findViewById(R.id.dashboardImage);
        recyclerView = v.findViewById(R.id.emotionData);

        Glide.with(requireContext())
                .asGif()
                .load(R.drawable.face_waving_gif)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(dashboardImage);

        sendDataToBackend();

        return v;
    }

    public void sendDataToBackend() {
        String url = ipAddress + "get_dashboard_data";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody;
        formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", Services.userEmail)
                .build();


        // Build request
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (getContext() != null) {
                        Toast.makeText(requireContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    resetDisplay();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    requireActivity().runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(responseString);
                            String error = json.getString("error");
                            String errorDesc = json.getString("errorDesc");

                            JSONArray EmotionDataArray = json.getJSONArray("emotionData");
                            JSONArray emotionInfo = new JSONArray();
                            if (EmotionDataArray.length() > 0) {
                                emotionInfo = EmotionDataArray.getJSONArray(2);
                            }

                            String weakness = json.getString("weakSubject");
                            String weakSubjectOldMarks = json.getString("weakSubjectOldMarks");
                            String weakSubjectNewMarks = json.getString("weakSubjectNewMarks");

                            JSONObject marks = json.getJSONObject("marks");

                            displayResults(error, weakness, emotionInfo, weakSubjectOldMarks, weakSubjectNewMarks);
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (getContext() != null) {
                            Toast.makeText(requireContext(), "Unexpected response from server", Toast.LENGTH_SHORT).show();
                        }
                        resetDisplay();
                    });
                }
            }
        });
    }

    private void displayResults(String error, String weakness, JSONArray emotionInfo, String weakSubjectOldMarks, String weakSubjectNewMarks) {
        if (weakness.isEmpty()) {
            tvWeekness.setText("");
            tvWeaknessError.setVisibility(View.VISIBLE);
        } else {
            tvWeekness.setText(weakness);
            tvWeaknessError.setVisibility(View.GONE);
        }

        if (emotionInfo.length() == 0) {
            tvEmotionError.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(GONE);
        } else {
            tvEmotionError.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Set the adapter
            EmotionRowDashboardAdapter adapter = new EmotionRowDashboardAdapter(emotionInfo);
            recyclerView.setAdapter(adapter);
            // Set up layout
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
        }

        if (weakSubjectOldMarks.isEmpty()) {
            tvWeakSubjectOldMarks.setText("_");
        } else {
            tvWeakSubjectOldMarks.setText(weakSubjectOldMarks + "%");
        }

        if (weakSubjectNewMarks.isEmpty()) {
            tvWeakSubjectNewMarks.setText("_");
        } else {
            if (!weakSubjectOldMarks.isEmpty()) {
                if (Float.parseFloat(weakSubjectNewMarks) < Float.parseFloat(weakSubjectOldMarks)) {
                    tvWeakSubjectNewMarks.setTextColor(getResources().getColor(R.color.incorrect));
                } else {
                    tvWeakSubjectNewMarks.setTextColor(getResources().getColor(R.color.correct));
                }
            }
            tvWeakSubjectNewMarks.setText(weakSubjectNewMarks + "%");
        }
    }

    private void resetDisplay() {
        tvWeekness.setText("");
        tvWeaknessError.setVisibility(View.VISIBLE);
        tvEmotionError.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(GONE);
        tvWeakSubjectOldMarks.setText("_");
        tvWeakSubjectNewMarks.setText("_");
    }

    @Override
    public void onResume() {
        super.onResume();
        sendDataToBackend();
    }
}

