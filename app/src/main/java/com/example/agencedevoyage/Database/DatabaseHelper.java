package com.example.agencedevoyage.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    public static final String TABLE_RESERVATION = "reservation";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "fullName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_TRAVELERS = "travelers";
    public static final String COLUMN_PASSPORT = "passport";
    public static final String COLUMN_REQUIREMENTS = "requirements";

    // SQL for creating the table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_RESERVATION + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FULL_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_TRAVELERS + " INTEGER, " +
                    COLUMN_PASSPORT + " TEXT, " +
                    COLUMN_REQUIREMENTS + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATION);
        onCreate(db);
    }
}

