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
import com.example.agencedevoyage.Entity.User;

@Database(entities = {Complaint.class}, version = 3, exportSchema = false)

public abstract class AppDatabase_rahma extends RoomDatabase {
    private static AppDatabase_rahma instance;

    public abstract ComplaintDAO complaintDAO();

    public static synchronized AppDatabase_rahma getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase_rahma.class, "complaint_database")
                    .addMigrations(MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Adding rating and feedback columns with default values
            database.execSQL("ALTER TABLE complaint ADD COLUMN rating INTEGER DEFAULT -1 NOT NULL");
            database.execSQL("ALTER TABLE complaint ADD COLUMN feedback TEXT");
        }
    };
}
