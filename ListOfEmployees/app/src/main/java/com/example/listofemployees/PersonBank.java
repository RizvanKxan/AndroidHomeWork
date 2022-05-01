package com.example.listofemployees;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonBank {
    List<Person> mPersonList;
    private static PersonBank sPersonBank;
    private final Context mContext;

    public static PersonBank get(Context context) {
        if (sPersonBank == null) {
            sPersonBank = new PersonBank(context);
        }
        return sPersonBank;
    }

    private PersonBank(Context context) {
        mContext = context.getApplicationContext();
        mPersonList = new ArrayList<>();
        mPersonList.add(new Person("123", "321", Person.makeCalendar(1, 2, 1992), true));
        mPersonList.add(new Person("123", "321", Person.makeCalendar(1, 2, 1992), false));
        mPersonList.add(new Person("123", "321", Person.makeCalendar(1, 2, 1992), true));
        mPersonList.add(new Person("123", "321", Person.makeCalendar(1, 2, 1992), true));
    }

    public List<Person> getPersons() {
        return mPersonList;
    }

    public Person getPerson(UUID id) {
        for (Person person : mPersonList) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public void addPerson(Person person) {
        mPersonList.add(person);
    }

    public void editPerson(UUID id, String firstName, String secondName, boolean isFemale) {
        for (Person person : mPersonList) {
            if (person.getId().equals(id)) {
                person.setFirstName(firstName);
                person.setSecondName(secondName);
                person.setFemale(isFemale);
            }
        }
    }
}
