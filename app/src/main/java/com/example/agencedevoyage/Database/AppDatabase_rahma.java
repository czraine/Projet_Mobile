package com.example.agencedevoyage.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.agencedevoyage.Dao.ComplaintDAO;
import com.example.agencedevoyage.Domains.Complaint;


@Database(entities = {Complaint.class}, version = 2)
public abstract class AppDatabase_rahma extends RoomDatabase {
    private static AppDatabase_rahma instance;

    public abstract ComplaintDAO complaintDAO();

    public static synchronized AppDatabase_rahma getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase_rahma.class, "complaint_database")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Crée une nouvelle table temporaire avec la colonne 'description'
            database.execSQL("CREATE TABLE IF NOT EXISTS complaints_temp ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "title TEXT, "
                    + "status TEXT, "
                    + "description TEXT)");  // Colonne description ajoutée

            // Copie les données de l'ancienne table vers la nouvelle
            database.execSQL("INSERT INTO complaints_temp (id, title, status)"
                    + " SELECT id, title, status FROM complaints");

            // Supprime l'ancienne table
            database.execSQL("DROP TABLE complaints");

            // Renomme la nouvelle table temporaire
            database.execSQL("ALTER TABLE complaints_temp RENAME TO complaints");
        }
    };

}
