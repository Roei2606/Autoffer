package com.example.local_project_sdk.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.local_project_sdk.models.LocalProjectEntity;
import com.example.local_project_sdk.models.LocalItemEntity;


@Database(
        entities = {LocalProjectEntity.class, LocalItemEntity.class},
        version = 2,
        exportSchema = false
)
public abstract class LocalProjectDatabase extends RoomDatabase {

    private static volatile LocalProjectDatabase INSTANCE;

    public abstract LocalProjectDao projectDao();
    public abstract LocalItemDao itemDao();

    public static LocalProjectDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalProjectDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LocalProjectDatabase.class,
                            "local_projects_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

