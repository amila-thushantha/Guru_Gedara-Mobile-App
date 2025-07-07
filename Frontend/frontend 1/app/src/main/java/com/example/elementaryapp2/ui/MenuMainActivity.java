package com.example.elementaryapp2.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.database.DatabaseHelper;
import com.example.elementaryapp2.services.AlertDialogUtil;
import com.example.elementaryapp2.services.Services;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        String isFirstTimeLoggedIn = getIntent().getStringExtra("isFirstTimeLoggedIn");

        builder = new AlertDialog.Builder(this);

        databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        while (cursor.moveToNext()) {
            Services.userEmail = cursor.getString(0);
        }

        if (isFirstTimeLoggedIn != null && isFirstTimeLoggedIn.equals("False")) {
            Intent intent = new Intent(MenuMainActivity.this, AppWelcomeActivity.class);
            startActivity(intent);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else if (itemId == R.id.about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if (itemId == R.id.logout) {
            showLogoutAlert();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    public static void deleteAllImagesInDirectory(Context context) {
        // Get the directory
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");

        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            if (files != null) {
                // Loop through and delete each file
                for (File file : files) {
                    if (file.isFile()) {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            // Handle the case where the file could not be deleted
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            } else {
                System.err.println("Directory is empty or could not be listed.");
            }
        } else {
            System.err.println("Directory does not exist.");
        }
    }

    public void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuMainActivity.this);
        builder.setTitle("Log out")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ShowAlertDialog("Logging out...");
                        databaseHelper.deleteAllData();
                        deleteAllImagesInDirectory(MenuMainActivity.this);
                        Intent intent = new Intent(MenuMainActivity.this, SignInActivity.class);
                        startActivity(intent);
                        Toast.makeText(MenuMainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        closeAlertDialog();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void ShowAlertDialog(String header) {
        AlertDialogUtil.showLoadingDialog(this, header);
    }

    public void closeAlertDialog() {
        AlertDialogUtil.dismissLoadingDialog();
    }

}