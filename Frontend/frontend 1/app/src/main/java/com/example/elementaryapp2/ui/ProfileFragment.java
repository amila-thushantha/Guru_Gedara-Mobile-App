package com.example.elementaryapp2.ui;

import static com.example.elementaryapp2.services.Services.ipAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.elementaryapp2.R;
import com.example.elementaryapp2.database.DatabaseHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    DatabaseHelper databaseHelper;
    EditText nameInput;
    ImageView pfp;
    Button uploadPfpBtn, saveBtn;
    AlertDialog alertDialog;
    String email = null;
    TextView emailText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        saveBtn = v.findViewById(R.id.save_btn);
        uploadPfpBtn = v.findViewById(R.id.upload_pfp_btn);
        nameInput = v.findViewById(R.id.nameInput);
        emailText = v.findViewById(R.id.email);
        pfp = v.findViewById(R.id.pfp);
        databaseHelper = new DatabaseHelper(this.getContext());
        Cursor cursor = databaseHelper.getAllData();
        while (cursor.moveToNext()) {
            String filePath = cursor.getString(2);
            if (!Objects.equals(filePath, "")) {
                Uri fileUri = getUriFromFilePath(getContext(), filePath);
//                pfp.setImageURI(fileUri);
                Glide.with(requireContext())
                        .load(fileUri)
                        .override(1080, 1080)
                        .centerCrop()
                        .skipMemoryCache(true) // Disable memory cache
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk cache
                        .into(pfp);


            } else {
                pfp.setImageResource(R.drawable.default_pfp);
            }

            nameInput.setText(cursor.getString(1));
            email = cursor.getString(0);
            emailText.setText(email);
        }

        // Set max length to 10 characters
        int maxLength = 25;
        nameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        // Capitalize first letter
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    // Capitalize first letter
                    String text = s.toString();
                    String capitalizedText = Character.toUpperCase(text.charAt(0)) + text.substring(1);
                    if (!capitalizedText.equals(text)) {
                        nameInput.setText(capitalizedText);
                        nameInput.setSelection(nameInput.length()); // Move cursor to end
                    }
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
        uploadPfpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(requireActivity())
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .cropSquare()
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                startForProfileImageResult.launch(intent);
                                return Unit.INSTANCE;
                            }
                        });
            }
        });

        return v;
    }

    public static Uri getUriFromFilePath(Context context, String filePath) {
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) {
//            throw new IllegalArgumentException("File does not exist: " + filePath);
            Toast.makeText(context, "File does not exist: " + filePath, Toast.LENGTH_SHORT).show();
        }

        // Return Uri for the file using FileProvider
        return FileProvider.getUriForFile(context, "com.example.elementaryapp2.fileprovider", file);
    }

    private ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK) {
                        // Image Uri will not be null for RESULT_OK
                        Uri fileUri = data != null ? data.getData() : null;

                        if (fileUri != null) {
//                            mProfileUri = fileUri;
                            if (email != null) {
                                File imageFile = saveImageToLocalDirectory(fileUri);
                                String imageFilePath = imageFile.getPath();
                                databaseHelper.updatePFPUrl(email, imageFilePath);
                                sendPfpToBackend(email, imageFile);
                            } else {
                                Toast.makeText(getContext(), "User data not available", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private File saveImageToLocalDirectory(Uri uri) {
        File directory = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, "profile_picture.jpg");
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Notify the media scanner about the new file
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            requireContext().sendBroadcast(intent);

            // Set the image URI to the profile image view
            pfp.setImageURI(null);
            pfp.setImageURI(Uri.fromFile(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void sendPfpToBackend(String email, File profilePicture) {
        ShowAlertDialog("Updating profile image...");
        String url = ipAddress + "set_pfp";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody;

        // Create request body
        formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("profile_picture", profilePicture.getName(),
                        RequestBody.create(profilePicture, MediaType.parse("image/jpeg")))
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
                    closeAlertDialog();
                    Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    requireActivity().runOnUiThread(() -> {
                        closeAlertDialog();
                        Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        closeAlertDialog();
                        Toast.makeText(getContext(), "Unexpected response from server", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    public void ShowAlertDialog(String header) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TransparentAlertDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.loading_dialog_layout, null);
        builder.setView(customLayout);
        TextView Header = customLayout.findViewById(R.id.loading_status);
        ImageView imageView = customLayout.findViewById(R.id.gifImageView);

        Glide.with(this)
                .asGif()
                .load(R.drawable.loading_gif)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);

        Header.setText(header);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void closeAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public void saveProfile() {
        String inputText = nameInput.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this.getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (email != null) {
                ShowAlertDialog("Updating profile name...");
                String url = ipAddress + "set_username";
                OkHttpClient client = new OkHttpClient();

                RequestBody formBody;

                // Create request body
                formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("email", email)
                        .addFormDataPart("name", inputText)
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
                            closeAlertDialog();
                            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            try {
                                JSONObject json = new JSONObject(responseString);
                                String error = json.getString("error");
                                String errorDesc = json.getString(("errorDesc"));

                                if (error.equals("0")) {
                                    requireActivity().runOnUiThread(() -> {
                                        databaseHelper.updateUserName(email, inputText);
                                        nameInput.setText(inputText);
                                        Toast.makeText(getContext(), "Profile saved", Toast.LENGTH_SHORT).show();

                                        closeAlertDialog();
                                    });
                                } else {
                                    Toast.makeText(getContext(), ""+errorDesc, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                closeAlertDialog();
                                Toast.makeText(getContext(), "Unexpected response from server", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "User data not available", Toast.LENGTH_SHORT).show();
            }
        }
    }
}