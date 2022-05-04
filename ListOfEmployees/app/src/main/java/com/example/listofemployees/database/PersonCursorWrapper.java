package com.example.listofemployees.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.listofemployees.Person;
import com.example.listofemployees.database.PersonDbScheme.PersonTable;

import java.util.UUID;

public class PersonCursorWrapper extends CursorWrapper {
    public PersonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Person getPerson() {
        String uuidString = getString(getColumnIndex(PersonTable.Cols.UUID));
        String firstName = getString(getColumnIndex(PersonTable.Cols.FIRST_NAME));
        String secondName = getString(getColumnIndex(PersonTable.Cols.SECOND_NAME));
        String date = getString(getColumnIndex(PersonTable.Cols.BIRTHDAY));
        int isFemale = getInt(getColumnIndex(PersonTable.Cols.FEMALE));

        Person person = new Person(UUID.fromString(uuidString));
        person.setFirstName(firstName);
        person.setSecondName(secondName);
        person.setFemale(isFemale != 0);
        person.setDate(date);
        return person;
    }
}
