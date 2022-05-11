package com.example.listofemployees;

import android.app.Application;

import androidx.room.Room;

import com.example.listofemployees.database.AppDatabase;

public class App extends Application {
    public static App instance;
    private AppDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
