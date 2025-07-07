package com.example.elementaryapp2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserData.db";
    public static final String TABLE_NAME = "User_Data_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USER_NAME";
    public static final String COL_3 = "PFP_URL";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " TEXT PRIMARY KEY, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert data method
    public boolean insertData(String id, String userName, String pfpPath) {
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_2, userName);
            contentValues.put(COL_3, pfpPath);
            return database.insert(TABLE_NAME, null, contentValues) != -1;
        }
    }

    // Retrieve all data method
    public Cursor getAllData() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Check if any user is logged in
    public boolean isLoggedIn() {
        try (SQLiteDatabase database = this.getReadableDatabase();
             Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;
            }
        }
        return false;
    }

    // Update user name method
    public boolean updateUserName(String id, String userName) {
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, userName);
            return database.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id}) > 0;
        }
    }

    // Update profile picture URL method
    public boolean updatePFPUrl(String id, String pfpURL) {
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_3, pfpURL);
            return database.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id}) > 0;
        }
    }

    // Delete user data method
    public boolean deleteData(String id) {
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            return database.delete(TABLE_NAME, COL_1 + " = ?", new String[]{id}) > 0;
        }
    }

    // Delete all data method
    public void deleteAllData() {
        try (SQLiteDatabase database = this.getWritableDatabase()) {
            database.delete(TABLE_NAME, null, null);
        }
    }
}
