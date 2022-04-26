package com.example.listofemployees;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
        mPersonList.add(new Person("123","321",Person.makeCalendar(1,2,1992), true));
        mPersonList.add(new Person("123","321",Person.makeCalendar(1,2,1992), false));
        mPersonList.add(new Person("123","321",Person.makeCalendar(1,2,1992), true));
        mPersonList.add(new Person("123","321",Person.makeCalendar(1,2,1992), true));
    }

    public List<Person> getPersons() {
        return mPersonList;
    }
}
