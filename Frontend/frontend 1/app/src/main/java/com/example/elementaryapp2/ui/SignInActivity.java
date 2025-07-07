package com.example.elementaryapp2.ui;

import static com.example.elementaryapp2.services.Services.ipAddress;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.database.DatabaseHelper;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.PasswordEditText;
import com.example.elementaryapp2.services.Services;
import com.example.elementaryapp2.services.SettingsDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {

    EditText emailInput;
    TextView registerLink;
    PasswordEditText passwordInput;
    Button signInBtn;
    AlertDialog alertDialog;
    DatabaseHelper databaseHelper;
    ImageButton settingsBtn;
    ScrollView signInContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signInBtn = findViewById(R.id.sign_in_btn);
        registerLink = findViewById(R.id.register_link);
        settingsBtn = findViewById(R.id.settings_btn);
        signInContent = findViewById(R.id.sign_in_content);

        Services.createIpKey(this);

        databaseHelper = new DatabaseHelper(this);
        if(databaseHelper.isLoggedIn()) {
            Intent intent = new Intent(SignInActivity.this, MenuMainActivity.class);
            startActivity(intent);
            finish();
        }


        passwordInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of SettingsDialogFragment
                SettingsDialogFragment settingsDialog = new SettingsDialogFragment();

                // Show the dialog
                settingsDialog.show(getSupportFragmentManager(), "SettingsDialog");
            }
        });


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void signIn () {
        String emailText = emailInput.getText().toString();
        String passwordText = passwordInput.getText().toString();

        if (emailText.isEmpty() && passwordText.isEmpty()) {
            Toast.makeText(this, "Input fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (passwordText.length() != 8) {
            Toast.makeText(this, "Password should be 8 characters long", Toast.LENGTH_SHORT).show();
        } else {
            ShowAlertDialog("Signing in...");
            sendDataToBackend(emailText, passwordText, new DataCallback() {
                @Override
                public void onSuccess(String message, String username, String image_path, String firstTimeLoggedIn) {
                    runOnUiThread(() -> {
                        databaseHelper.insertData(emailText, username, image_path);

                        closeAlertDialog();
                        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignInActivity.this, MenuMainActivity.class);
                        intent.putExtra("isFirstTimeLoggedIn", firstTimeLoggedIn);
                        startActivity(intent);


                        clearInputs(emailInput);
                        clearInputs(passwordInput);

                        finish();
                    });
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        closeAlertDialog();
                        Toast.makeText(SignInActivity.this, error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
    public interface DataCallback {
        void onSuccess(String message, String username, String image_path, String firstTimeLoggedIn);
        void onFailure(String error);
    }


    public void sendDataToBackend(String email, String password, DataCallback callback) {
        String url = ipAddress + "sign_in";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
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
                    callback.onFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    SignInActivity.this.runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(responseString);
                            String error = json.getString("error");
                            String errorDesc = json.getString("errorDesc");
                            String username = json.getString("username");
                            String firstTimeLoggedIn = json.getString("isFirstTimeLoggedIn");

                            if (error.equals("0")) {
                                fetchProfilePicture(email, "Sign in successful", username, callback, firstTimeLoggedIn);
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

    public void fetchProfilePicture(String email, String message, String username, DataCallback callback, String firstTimeLoggedIn) {
        String url = ipAddress + "get_pfp";
        OkHttpClient client = new OkHttpClient();

        // Create request body
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .build();

        // Build request
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    callback.onFailure(e.getMessage());
//                    Toast.makeText(SignInActivity.this, "Request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String contentType = response.header("Content-Type");
                    if (contentType != null) {
                        if (contentType.contains("image")) {
                            InputStream inputStream = response.body().byteStream();
                            File imageFile = saveImageToFile(inputStream);
                            String image_path = imageFile.getPath();
                            runOnUiThread(() -> {
                                callback.onSuccess(message, username, image_path, firstTimeLoggedIn);
                            });
                        } else if (contentType.contains("application/json")) {
                            callback.onSuccess(message, username, "", firstTimeLoggedIn);
                        }
                    }

                } else {
                    callback.onFailure("Server failed");
                }
            }
        });
    }

    public File saveImageToFile(InputStream inputStream) {
        File file = null;
        FileOutputStream outputStream = null;
        try {
            // Create a file in the app's external files directory
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");

            // Ensure the directory exists
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            file = new File(directory, "profile_picture.jpg");
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;

            // Write the InputStream to the file
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            Log.e("SaveImage", "Error saving image", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("SaveImage", "Error closing output stream", e);
                }
            }
        }

        return file;
    }


    public void ShowAlertDialog(String header) {
        // Show the dialog
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