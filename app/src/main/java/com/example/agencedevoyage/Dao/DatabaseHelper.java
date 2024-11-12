package com.example.agencedevoyage.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.agencedevoyage.Domains.Post;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "travelAgency.db";
    private static final int DATABASE_VERSION = 3;

    // Table posts
    public static final String TABLE_POSTS = "posts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGE_URL = "image_url";

    // Table comments
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_COMMENT_ID = "comment_id";
    public static final String COLUMN_POST_ID = "post_id";
    public static final String COLUMN_COMMENT_TEXT = "comment_text";

    // Table creation SQL
    private static final String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_IMAGE_URL + " TEXT);";

    private static final String CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS + " (" +
            COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_POST_ID + " INTEGER, " +
            COLUMN_COMMENT_TEXT + " TEXT, " +
            "FOREIGN KEY(" + COLUMN_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + COLUMN_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Migration steps for version 2, if any
        }
        if (oldVersion < 3) {
            db.execSQL(CREATE_TABLE_COMMENTS);
        }
    }

    // Méthode pour insérer un commentaire pour un post spécifique
    public void insertComment(int postId, String commentText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_ID, postId);
        values.put(COLUMN_COMMENT_TEXT, commentText);

        long result = db.insert(TABLE_COMMENTS, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Erreur lors de l'insertion du commentaire.");
        } else {
            Log.d("DatabaseHelper", "Commentaire inséré avec succès pour le post ID: " + postId);
        }
    }

    // Méthode pour récupérer tous les commentaires associés à un post
    public List<String> getCommentsByPostId(int postId) {
        List<String> comments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMMENTS,
                new String[]{COLUMN_COMMENT_TEXT},
                COLUMN_POST_ID + " = ?",
                new String[]{String.valueOf(postId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int commentIndex = cursor.getColumnIndex(COLUMN_COMMENT_TEXT);
                if (commentIndex != -1) {
                    comments.add(cursor.getString(commentIndex));
                }
            }
            cursor.close();
        }
        return comments;
    }

    // Méthode pour récupérer un post par son ID, en incluant les commentaires
    public Post getPostById(int postId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(postId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Post post = new Post();

            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            int imageUrlIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);

            if (idIndex != -1) post.setId(cursor.getInt(idIndex));
            if (descriptionIndex != -1) post.setDescription(cursor.getString(descriptionIndex));
            if (dateIndex != -1) post.setDate(cursor.getString(dateIndex));
            if (imageUrlIndex != -1) post.setImageUrl(cursor.getString(imageUrlIndex));

            // Récupérer les commentaires associés et les ajouter au post
            List<String> comments = getCommentsByPostId(postId);
            post.setComments(comments);

            cursor.close();
            return post;
        }

        if (cursor != null) cursor.close();
        return null; // Retourne null si le post n'est pas trouvé
    }

    public void updatePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Mise à jour de la description du post
        values.put(COLUMN_DESCRIPTION, post.getDescription());

        // Mise à jour du post dans la base de données
        int rowsAffected = db.update(TABLE_POSTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(post.getId())});
        if (rowsAffected > 0) {
            Log.d("DatabaseHelper", "Post mis à jour avec succès.");
        } else {
            Log.e("DatabaseHelper", "Erreur lors de la mise à jour du post.");
        }
    }
}
