package com.example.unamed.mvc.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.unamed.mvc.content.Item;

import java.util.concurrent.atomic.AtomicInteger;

@Database(entities = {Item.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance = null;
    public static AtomicInteger mDatabaseIndex = new AtomicInteger(0);

    public abstract SerieDao userDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "serie")
                .fallbackToDestructiveMigration().build();
    }
}
