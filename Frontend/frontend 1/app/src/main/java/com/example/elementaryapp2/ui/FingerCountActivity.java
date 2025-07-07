package com.example.elementaryapp2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.CameraFragment.CameraFragment;
import com.example.elementaryapp2.services.CameraFragment.CameraResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class FingerCountActivity extends AppCompatActivity implements CameraResponseListener {

    private static final String ARG_PARAM_TYPE = "ARG_PARAM_TYPE";
    private static final String ARG_PARAM_SCREEN_NAME = "ARG_PARAM_SCREEN_NAME";
    private static final int INTERVAL = 10000; // 10 seconds

    private String type;
    private String screenName;
    private int gestureCount = 5;
    private int currentGestureIndex = 0;
    private Handler handler;
    private Runnable gestureRunnable;

    private final Random random = new Random();
    private final String[] gestureClass = {
            "finger_0", "finger_1", "finger_2", "finger_3", "finger_4",
            "finger_5"
    };

    private String[] selectedGestures;

    private ImageView gestureImage;
    private TextView gestureNumber, gestureAllCount;
    private CardView gestureSection;
    private CameraFragment cameraFragment;

    @Override
    public void onCameraResponseReceived(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        String error = json.getString("error");
        String errorDesc = json.getString("errorDesc");

//            JSONArray gestureDataArray = json.getJSONArray("gestureData");
//            JSONArray selectedGestureArray = json.getJSONArray("selectedGestures");
        String score = json.getString("score");
        AlertDialogUtil.ShowFingerCountingAlertDialog(this, score, error, R.color.bgClr_1_dark, isClicked -> {
            if (isClicked) {
                Intent intent = new Intent(FingerCountActivity.this, MenuMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onCameraRecordStarted(Boolean response) {
        handleResponseVideoCapture(response);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_count);

        initViews();
        retrieveArguments();
        setupGestureSection();
    }

    private void initViews() {
        gestureImage = findViewById(R.id.gesture_image);
        gestureSection = findViewById(R.id.gesture_section);
        gestureAllCount = findViewById(R.id.gesture_all_count);
        gestureNumber = findViewById(R.id.gesture_number);

        gestureSection.setVisibility(View.GONE);
    }

    private void retrieveArguments() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra(ARG_PARAM_TYPE);
            screenName = getIntent().getStringExtra(ARG_PARAM_SCREEN_NAME);
        }
    }

    private void setupGestureSection() {
        selectedGestures = generateGestures();
        cameraFragment = CameraFragment.newInstance(type, screenName, String.join(" ", selectedGestures), 0);
        addCameraFragment();
    }

    private String[] generateGestures() {
        ArrayList<Integer> usedIndexes = new ArrayList<>();
        String[] gestures = new String[gestureCount];

        for (int i = 0; i < gestureCount; i++) {
            gestures[i] = gestureClass[generateUniqueIndex(usedIndexes)];
        }

        return gestures;
    }

    private int generateUniqueIndex(ArrayList<Integer> usedIndexes) {
        int index;
        do {
            index = random.nextInt(gestureClass.length);
        } while (usedIndexes.contains(index));
        usedIndexes.add(index);
        return index;
    }

    private void handleResponseVideoCapture(Boolean response) {
        if (response) {
            startGestureRunnable();
        } else {
            stopGestureRunnable();
        }
    }

    private void startGestureRunnable() {
        if (handler == null) {
            handler = new Handler();
            gestureRunnable = this::runGestureSequence;
            handler.post(gestureRunnable);
            gestureSection.setVisibility(View.VISIBLE);
            gestureAllCount.setText(String.valueOf(gestureCount));
        }
    }

    private void stopGestureRunnable() {
        if (handler != null) {
            handler.removeCallbacks(gestureRunnable);
            handler = null;
            gestureSection.setVisibility(View.GONE);
        }
    }

    private void runGestureSequence() {
        if (currentGestureIndex < gestureCount) {
            String gesture = selectedGestures[currentGestureIndex];
            int imageResource = getResources().getIdentifier(gesture, "drawable", getPackageName());
            gestureImage.setImageResource(imageResource);

            currentGestureIndex++;
            gestureNumber.setText(String.valueOf(currentGestureIndex));
            handler.postDelayed(gestureRunnable, INTERVAL);
        } else {
            stopGestureRunnable();
        }
    }

    private void addCameraFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.camera_fragment_container, cameraFragment);
        transaction.commitAllowingStateLoss();
    }

    private void removeCameraFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(cameraFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGestureRunnable();
        removeCameraFragment();
    }
}
