package com.example.elementaryapp2.services.CameraFragment;

import static com.example.elementaryapp2.services.Services.ipAddress;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.elementaryapp2.R;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.Services;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CameraFragment extends Fragment {

    ExecutorService service;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    ImageButton capture, toggleFlash, flipCamera;
    PreviewView previewView;

    private ImageCapture imageCapture;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    private Camera camera = null;
    private ProcessCameraProvider cameraProvider = null;
    String url;

    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private String type;
    private String screenName;
    private String mainParam;
    private int timeoutForFaceCapture;
    private View captureArea;
    AlertDialog alertDialog;
    private Handler handler = null;
    private Runnable timeoutRunnable;
    private int TIMEOUT = 50000; // 50 seconds in milliseconds
    boolean isRecording = false;

    private CameraResponseListener responseListener;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (Objects.equals(type, "video")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCamera(cameraFacing);
            } else if (Objects.equals(type, "video_without_preview")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCameraWithoutPreview(cameraFacing);
            } else {
                startPhotoCamera(cameraFacing);
            }
        }
    });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CameraResponseListener) {
            responseListener = (CameraResponseListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CameraResponseListener");
        }
    }



    public static CameraFragment newInstance(String type, String screenName, String mainParam, int timeoutForFaceCapture) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, screenName);
        args.putString(ARG_PARAM3, mainParam);
        args.putInt(ARG_PARAM4, timeoutForFaceCapture);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
            screenName = getArguments().getString(ARG_PARAM2);
            mainParam = getArguments().getString(ARG_PARAM3);
            timeoutForFaceCapture = getArguments().getInt(ARG_PARAM4);
        }
//        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

//        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                if (Objects.equals(type, "video")) {
//                    startVideoCamera(cameraFacing);
//                } else {
//                    startPhotoCamera(cameraFacing);
//                }
//            }
//        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        previewView = v.findViewById(R.id.viewFinder);
        capture = v.findViewById(R.id.capture);
        toggleFlash = v.findViewById(R.id.toggleFlash);
        flipCamera = v.findViewById(R.id.flipCamera);


        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                captureVideo();
                isRecording = false;
                handler = null;
            }
        };

        capture.setOnClickListener(view -> {
            if (!hasPermissions()) {
                requestPermissions();
            } else {
                if (Objects.equals(type, "video") && !isRecording) {
                    handler = new Handler();
                    captureVideo();
                    isRecording = true;
                    Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();
                    // Start the timeout
                    handler.postDelayed(timeoutRunnable, TIMEOUT);
                } else {
                    captureImage();
                }
            }
        });

        if (!hasPermissions()) {
            requestPermissions();
        } else {
            if (Objects.equals(type, "video")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCamera(cameraFacing);
            } else if (Objects.equals(type, "video_without_preview")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCameraWithoutPreview(cameraFacing);
            } else {
                startPhotoCamera(cameraFacing);
            }
        }

        if (Objects.equals(type, "image")) {
            capture.setImageResource(R.drawable.round_fiber_manual_image_capture_24);
        }

        if (Objects.equals(screenName, "count_screen")) {
            url = ipAddress + "analyze_finger_count";
//            captureArea = v.findViewById(R.id.capture_area_clock);
//            captureArea.setVisibility(View.VISIBLE);
        } else if (Objects.equals(screenName, "emotion_screen")) {
            url = ipAddress + "analyze_emotions";
            TIMEOUT = timeoutForFaceCapture;
        }

        flipCamera.setOnClickListener(view -> {
            if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }

            if (Objects.equals(type, "video")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCamera(cameraFacing);
            } else if (Objects.equals(type, "video_without_preview")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCameraWithoutPreview(cameraFacing);
            } else {
                startPhotoCamera(cameraFacing);
            }
        });

        service = Executors.newSingleThreadExecutor();
        return v;
    }

    public void startRecording() {
        if (!isRecording) {
            handler = new Handler();
            captureVideo();
            isRecording = true;
            Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();
            // Start the timeout
            handler.postDelayed(timeoutRunnable, TIMEOUT);
        }
    }

    public void captureVideo() {
//        capture.setImageResource(R.drawable.round_stop_circle_24);
        if (Objects.equals(type, "video")) {
            Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            capture.startAnimation(blinkAnimation);
        }

//        sharedViewModelVideoCapture.setResponse("True");
        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }
        responseListener.onCameraRecordStarted(true);
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions.Builder(requireContext().getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues).build();

//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        recording = videoCapture.getOutput().prepareRecording(requireContext(), options).start(ContextCompat.getMainExecutor(requireContext()), videoRecordEvent -> {
            if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                capture.setEnabled(true);
            } else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                if (!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                    Uri videoPath = ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();
                    String msg = "Video captured";
                    if (getContext() != null) {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    camera.getCameraControl().enableTorch(false);
                    isRecording =false;
//                    sharedViewModelVideoCapture.setResponse("False");
                    responseListener.onCameraRecordStarted(false);
                    sendCapturedVideo(videoPath);
                } else {
                    if (recording != null) {
                        recording.close();
                        recording = null;
                    }
                    String msg = "Error: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError();
                    if (getContext() != null) {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
//                    sharedViewModelVideoCapture.setResponse("False");
                    responseListener.onCameraRecordStarted(false);
                    if (handler != null) {
                        handler.removeCallbacks(timeoutRunnable);
                    }
                    capture.clearAnimation();
                    isRecording =false;
                }
//                capture.setImageResource(R.drawable.round_fiber_manual_record_24);
                if (capture.getAnimation() != null && capture.getAnimation().hasStarted() && !capture.getAnimation().hasEnded()) {
                    // An animation is currently active on the view
                    capture.clearAnimation();
                }
            }
        });
    }

    public void captureImage() {
        if (imageCapture == null) {
            Toast.makeText(getContext(), "Camera not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for necessary permissions (ensure permissions are handled in your app)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Camera permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images");

        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(
                requireContext().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri imageUri = outputFileResults.getSavedUri();
                if (imageUri != null) {
//                    String msg = "Image captured: " + imageUri.toString();
                    Toast.makeText(getContext(), "Image captured", Toast.LENGTH_LONG).show();
                    // Optional: Share or display the captured image
                } else {
                    Toast.makeText(getContext(), "Image capture failed: Uri is null", Toast.LENGTH_SHORT).show();
                }
                camera.getCameraControl().enableTorch(false);
                cropAndSendImage(imageUri);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                String msg = "Image capture failed: " + exception.getMessage();
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void startVideoCamera(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(requireContext());

        processCameraProvider.addListener(() -> {
            try {
                cameraProvider = processCameraProvider.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);

                toggleFlash.setOnClickListener(view -> toggleFlash(camera));

                if (camera.getCameraInfo().getTorchState().getValue() == TorchState.ON) {
                    toggleFlash.setImageResource(R.drawable.round_flash_on_24);
                } else {
                    toggleFlash.setImageResource(R.drawable.round_flash_off_24);
                }

                if (!camera.getCameraInfo().hasFlashUnit()) {
                    toggleFlash.setVisibility(View.GONE);
                } else {
                    toggleFlash.setVisibility(View.VISIBLE);
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    public void startVideoCameraWithoutPreview(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(requireContext());

        processCameraProvider.addListener(() -> {
            try {
                cameraProvider = processCameraProvider.get();

                // Remove the Preview use case
                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                // Bind only the VideoCapture use case
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, videoCapture);

                toggleFlash.setOnClickListener(view -> toggleFlash(camera));

                if (camera.getCameraInfo().getTorchState().getValue() == TorchState.ON) {
                    toggleFlash.setImageResource(R.drawable.round_flash_on_24);
                } else {
                    toggleFlash.setImageResource(R.drawable.round_flash_off_24);
                }

                if (!camera.getCameraInfo().hasFlashUnit()) {
                    toggleFlash.setVisibility(View.GONE);
                } else {
                    toggleFlash.setVisibility(View.VISIBLE);
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }



    public void startPhotoCamera(int cameraFacing) {
        if (cameraProvider != null) {
            Toast.makeText(getContext(), "Camera is already initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(requireContext());

        processCameraProvider.addListener(() -> {
            try {
                cameraProvider = processCameraProvider.get();

                // Set up preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Set up video capture
                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                // Set up image capture
                imageCapture = new ImageCapture.Builder().build();

                cameraProvider.unbindAll(); // Unbind use cases before rebinding

                // Create camera selector
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                // Bind use cases to the lifecycle
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture, imageCapture);

                // Set up flash toggle listener
                toggleFlash.setOnClickListener(view -> toggleFlash(camera));

                // Update flash icon based on current state
                updateFlashIcon();

                // Check if flash unit is available
                if (!camera.getCameraInfo().hasFlashUnit()) {
                    toggleFlash.setVisibility(View.GONE);
                } else {
                    toggleFlash.setVisibility(View.VISIBLE);
                }
            } catch (ExecutionException e) {
                Toast.makeText(getContext(), "Camera initialization failed: " + e.getCause().getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                Toast.makeText(getContext(), "Camera initialization was interrupted", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void updateFlashIcon() {
        if (camera.getCameraInfo().getTorchState().getValue() == TorchState.ON) {
            toggleFlash.setImageResource(R.drawable.round_flash_on_24);
        } else {
            toggleFlash.setImageResource(R.drawable.round_flash_off_24);
        }
    }



    private void toggleFlash(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            toggleFlash.setVisibility(View.VISIBLE);

            if (camera.getCameraInfo().getTorchState().getValue() == TorchState.ON) {
                camera.getCameraControl().enableTorch(false);
                toggleFlash.setImageResource(R.drawable.round_flash_off_24);
            } else {
                camera.getCameraControl().enableTorch(true);
                toggleFlash.setImageResource(R.drawable.round_flash_on_24);
            }
        } else {
            toggleFlash.setVisibility(View.GONE);
        }
    }

    public void sendCapturedImageClock(Uri imageUri) {
        ShowAlertDialog("Analyzing...");

        String imagePath = getRealPathFromURI(imageUri, "image");
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            Toast.makeText(getContext(), "Image file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an OkHttpClient with timeout settings
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                .addFormDataPart("mainParam", mainParam)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "Failed to send image", Toast.LENGTH_SHORT).show();
                    closeAlertDialog();
                });
                imageFile.delete();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();

                    requireActivity().runOnUiThread(() -> {
                        // Update the ViewModel with the response
                        if (Objects.equals(screenName, "clock_screen")) {
                            closeAlertDialog();
//                            sharedViewModelClock.setResponse(responseString);
                        }
                    });
                    // Delete the image file
                    imageFile.delete();
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Failed to send image", Toast.LENGTH_SHORT).show();
                        closeAlertDialog();
                    });
                    imageFile.delete();
                }
            }
        });
    }

    private void sendCapturedVideo(Uri videoUri) {
        ShowAlertDialog("Analyzing...");

        String videoPath = getRealPathFromURI(videoUri, "video");
        File videoFile = new File(videoPath);

        if (videoFile.exists()) {
            // Create an OkHttpClient with timeout settings
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS)
                    .build();

            RequestBody videoBody = RequestBody.create(videoFile, MediaType.parse("video/mp4"));

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("video", videoFile.getName(), videoBody)
                    .addFormDataPart("mainParam", mainParam)
                    .addFormDataPart("email", Services.userEmail)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d("CameraFragment", "" + e);
                        Toast.makeText(getContext(), "Video upload failed" , Toast.LENGTH_SHORT).show();
                        closeAlertDialog();
                    });
                    videoFile.delete();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseString = response.body().string();

                        requireActivity().runOnUiThread(() -> {
                            closeAlertDialog();
                            // Update the ViewModel with the response
//                            sharedViewModelEmoGesture.setResponse(responseString);
                            try {
                                responseListener.onCameraResponseReceived(responseString);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        videoFile.delete();
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(getContext(), "Video upload failed", Toast.LENGTH_SHORT).show();
                            closeAlertDialog();
                        });
                        videoFile.delete();

                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Video file not found", Toast.LENGTH_SHORT).show();
            closeAlertDialog();
        }
    }

    private String getRealPathFromURI(Uri contentUri, String type) {
        if (Objects.equals(type, "video")) {
            String[] proj = {MediaStore.Video.Media.DATA};
            Cursor cursor = requireContext().getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                return path;
            }
            return null;
        } else {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = requireContext().getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                return path;
            }
            return null;
        }

    }



    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API level 33)
            activityResultLauncher.launch(Manifest.permission.CAMERA);
//            activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
            activityResultLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO);
//            activityResultLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            // For older versions
            activityResultLauncher.launch(Manifest.permission.CAMERA);
//            activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
            activityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }


    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
//                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    (Build.VERSION.SDK_INT > Build.VERSION_CODES.P || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
    }

    private void cropAndSendImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            Rect captureRect = getCaptureRect(bitmap.getWidth(), bitmap.getHeight());

            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, captureRect.left, captureRect.top, captureRect.width(), captureRect.height());

            // Save the cropped image
            String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis());
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + "_cropped");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images");

            Uri croppedImageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            OutputStream outputStream = requireContext().getContentResolver().openOutputStream(croppedImageUri);
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            if (Objects.equals(screenName, "clock_screen")) {
                sendCapturedImageClock(croppedImageUri);
            } else if (Objects.equals(screenName, "word_screen")) {
//                sendCapturedImageWord(croppedImageUri);
            }


            String imagePath = getRealPathFromURI(imageUri, "image");
            File imageFile = new File(imagePath);
            imageFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Rect getCaptureRect(int originalImageWidth, int originalImageHeight) {
        // Get the size of the preview view and the capture area
        int previewWidth = previewView.getWidth();
        int previewHeight = previewView.getHeight();

        int[] location = new int[2];
        captureArea.getLocationOnScreen(location);

        int left = location[0];
        int top = location[1];
        int width = captureArea.getWidth();
        int height = captureArea.getHeight();

        // Ensure location is relative to preview view
        int[] previewLocation = new int[2];
        previewView.getLocationOnScreen(previewLocation);

        left -= previewLocation[0];
        top -= previewLocation[1];

        // Scale the coordinates to the size of the original image
        float scaleX = (float) originalImageWidth / previewWidth;
        float scaleY = (float) originalImageHeight / previewHeight;

        left = Math.round(left * scaleX);
        top = Math.round(top * scaleY);
        width = Math.round(width * scaleX);
        height = Math.round(height * scaleY);

        // Ensure the coordinates are within the bounds of the original image
        left = Math.max(0, left);
        top = Math.max(0, top);
        width = Math.min(width, originalImageWidth - left);
        height = Math.min(height, originalImageHeight - top);

        return new Rect(left, top, left + width, top + height);
    }


    private void resetCameraServices() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        if (recording != null) {
            recording.stop();
            recording = null;
        }
        service.shutdown();
    }

    public void ShowAlertDialog(String header) {
        AlertDialogUtil.showLoadingDialog(getContext(), header);
    }

    public void closeAlertDialog() {
        AlertDialogUtil.dismissLoadingDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetCameraServices();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasPermissions()) {
            if (Objects.equals(type, "video")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCamera(cameraFacing);
            } else if (Objects.equals(type, "video_without_preview")) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
                startVideoCameraWithoutPreview(cameraFacing);
            } else {
                startPhotoCamera(cameraFacing);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ProcessCameraProvider cameraProvider = null;
        try {
            cameraProvider = ProcessCameraProvider.getInstance(requireContext()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        cameraProvider.unbindAll();
    }

}
