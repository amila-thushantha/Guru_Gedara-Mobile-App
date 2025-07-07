package com.example.elementaryapp2.services.CameraFragment;

import org.json.JSONException;

public interface CameraResponseListener {
    void onCameraResponseReceived(String response) throws JSONException;
    void onCameraRecordStarted(Boolean response);
}

