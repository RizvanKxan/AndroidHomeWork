package com.example.listofemployees.database.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.listofemployees.database.entity.Person;

import java.util.List;
import java.util.UUID;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM persons")
    List<Person> getAll();

    @Query("SELECT * FROM persons WHERE uuid = :uuid")
    Person getById(UUID uuid);

    @Insert(onConflict = REPLACE)
    void insertPerson(Person person);

    @Delete
    void deletePerson(Person person);

    @Update
    void updatePerson(Person person);
}
