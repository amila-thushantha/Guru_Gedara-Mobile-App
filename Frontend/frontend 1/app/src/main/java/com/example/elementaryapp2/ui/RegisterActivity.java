package com.example.elementaryapp2.ui;

import static com.example.elementaryapp2.services.Services.ipAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.database.DatabaseHelper;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.PasswordEditText;
import com.github.dhaval2404.imagepicker.ImagePicker;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText nameInput, emailInput;
    PasswordEditText passwordInput, repasswordInput;
    ImageView pfp;
    Button uploadPfpBtn, createProfileBtn;
    AlertDialog alertDialog;
    File pfpFile = null;
    String selectedAccType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createProfileBtn = findViewById(R.id.save_btn);
        uploadPfpBtn = findViewById(R.id.upload_pfp_btn);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        repasswordInput = findViewById(R.id.repasswordInput);
        pfp = findViewById(R.id.pfp);
        databaseHelper = new DatabaseHelper(this);

        // Set max length to 10 characters
        nameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        repasswordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        // Capitalize first letter
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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

        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfile();
            }
        });

        uploadPfpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(RegisterActivity.this)
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
    }

    ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(
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
                            saveImageToLocalDirectory(fileUri);
                        }
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(RegisterActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void saveImageToLocalDirectory(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, "profile_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Notify the media scanner about the new file
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);

            // Set the image URI to the profile image view
            pfp.setImageURI(null);
            pfp.setImageURI(Uri.fromFile(file));
            pfpFile = file;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createProfile() {
        String nameText = nameInput.getText().toString();
        String passwordText = passwordInput.getText().toString();
        String repasswordText = repasswordInput.getText().toString();
        String emailText = emailInput.getText().toString();
        if (nameText.isEmpty() && passwordText.isEmpty() && repasswordText.isEmpty() && emailText.isEmpty()) {
            Toast.makeText(this, "Input fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (passwordText.length() != 8) {
            Toast.makeText(this, "Password should be 8 characters long", Toast.LENGTH_SHORT).show();
        } else if (!passwordText.equals(repasswordText)) {
            Toast.makeText(this, "Re-enter password correctly", Toast.LENGTH_SHORT).show();
        } else {
            ShowAlertDialog("Creating profile...");
            sendDataToBackend(nameText, emailText, passwordText, pfpFile, new DataCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {

                        closeAlertDialog();
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                        clearInputs(emailInput);
                        clearInputs(nameInput);
                        clearInputs(passwordInput);
                        clearInputs(repasswordInput);

//                        Intent intent = new Intent(RegisterActivity.this, MenuMainActivity.class);
//                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        closeAlertDialog();
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    public interface DataCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }


    public void sendDataToBackend(String name, String email, String password, File profilePicture, DataCallback callback) {
        String url = ipAddress + "register";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody;

        if (profilePicture != null) {
            // Create request body
            formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
                    .addFormDataPart("profile_picture", profilePicture.getName(),
                            RequestBody.create(profilePicture, MediaType.parse("image/jpeg")))
                    .build();
        } else {
            formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
                    .build();
        }

        // Build request
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    RegisterActivity.this.runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(responseString);
                            String error = json.getString("error");
                            String errorDesc = json.getString("errorDesc");

                            if (error.equals("0")) {
                                callback.onSuccess("Registration successful");
                            } else {
                                callback.onFailure(errorDesc);
                            }
                        } catch (JSONException e) {
                            callback.onFailure(e.getMessage());
                        }
                    });
                } else {
                    callback.onFailure("Unexpected response from server");
                }
            }
        });
    }

    public void ShowAlertDialog(String header) {
        AlertDialogUtil.showLoadingDialog(this, header);
    }

    public void closeAlertDialog() {
        AlertDialogUtil.dismissLoadingDialog();
    }

    public void clearInputs(EditText editText) {
        editText.setText("");
    }
    public void clearInputs(PasswordEditText editText) {
        editText.setText("");
    }
}