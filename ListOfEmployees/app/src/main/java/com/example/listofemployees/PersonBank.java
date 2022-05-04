package com.example.listofemployees;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.listofemployees.database.PersonBaseHelper;
import com.example.listofemployees.database.PersonCursorWrapper;
import com.example.listofemployees.database.PersonDbScheme.PersonTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonBank {
    private static PersonBank sPersonBank;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public static PersonBank get(Context context) {
        if (sPersonBank == null) {
            sPersonBank = new PersonBank(context);
        }
        return sPersonBank;
    }

    private PersonBank(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PersonBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();

        PersonCursorWrapper cursor = queryPersons(null,null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                persons.add(cursor.getPerson());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return persons;
    }

    private PersonCursorWrapper queryPersons(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PersonTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PersonCursorWrapper(cursor);
    }

    public Person getPerson(UUID id) {
        PersonCursorWrapper cursor = queryPersons(
                PersonTable.Cols.UUID + " = ?",
                new String[] { id.toString()}
        );

        try {
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPerson();
        } finally {
            cursor.close();
        }
    }

    public void updatePerson(Person person) {
        String uuidString = person.getId().toString();
        ContentValues values = getContentValues(person);

        mDatabase.update(PersonTable.NAME, values,
                PersonTable.Cols.UUID + " = ?",
                new String[] { uuidString});
    }

    public static ContentValues getContentValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonTable.Cols.UUID, person.getId().toString());
        values.put(PersonTable.Cols.FIRST_NAME, person.getFirstName());
        values.put(PersonTable.Cols.SECOND_NAME, person.getSecondName());
        values.put(PersonTable.Cols.FEMALE, person.isFemale ? 1 : 0);
        values.put(PersonTable.Cols.BIRTHDAY, person.getBirthDayString());
        return values;
    }

    public void addPerson(Person person) {
        ContentValues values = getContentValues(person);

        mDatabase.insert(PersonTable.NAME, null, values);
    }

    public void editPerson(UUID id, String firstName, String secondName, boolean isFemale) {
        Person person = getPerson(id);
        person.setFirstName(firstName);
        person.setSecondName(secondName);
        person.setFemale(isFemale);
        updatePerson(person);
    }

    public void deletePerson(UUID id) {
        mDatabase.delete(PersonTable.NAME,
                PersonTable.Cols.UUID + " = ?",
                new String[] { id.toString()});
    }
}
