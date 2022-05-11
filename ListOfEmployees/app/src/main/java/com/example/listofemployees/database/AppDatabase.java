package com.example.listofemployees.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.listofemployees.database.dao.PersonDao;
import com.example.listofemployees.database.entity.Person;

@Database(entities = {Person.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}
