package com.example.agencedevoyage.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.agencedevoyage.Entity.Offer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Offer.class}, version = 2, exportSchema = false)
public abstract class AppDatabase_firas extends RoomDatabase {
    private static AppDatabase_firas instance;

    // Define an executor with a fixed thread pool to run database operations
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public abstract com.example.agencedevoyage.Dao.OfferDao offerDao();

    // Singleton pattern to prevent having multiple instances of the database opened at the same time
    public static synchronized AppDatabase_firas getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase_firas.class, "offer_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
