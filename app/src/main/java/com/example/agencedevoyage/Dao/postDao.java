package com.example.agencedevoyage.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agencedevoyage.Domains.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class postDao {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public postDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Add a new post with the current date and auto-generated ID
    public long addPost(Post post) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, post.getDescription());
        values.put(DatabaseHelper.COLUMN_DATE, currentDate);
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, post.getImageUrl());

        return database.insert(DatabaseHelper.TABLE_POSTS, null, values);
    }

    public Post getPostById(int id) {
        Log.d("postDao", "Querying for post with ID: " + id);

        Cursor cursor = database.query(DatabaseHelper.TABLE_POSTS, null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Post post = new Post();
            post.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            post.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)));
            post.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
            post.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL)));
            cursor.close();
            return post;
        }

        if (cursor != null) cursor.close();
        return null;
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_POSTS, null, null, null, null, null, DatabaseHelper.COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post();
                post.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                post.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)));
                post.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
                post.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL)));
                posts.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return posts;
    }
}
