package com.example.listofemployees;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.UUID;

public class Person {
    private final UUID id;
    private String firstName;
    private String secondName;
    private final Calendar birthDay;
    public boolean isFemale;
    public Person(String firstName, String secondName, Calendar birthDay, boolean isFemale) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDay = birthDay;
        this.isFemale = isFemale;
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

    //--- Приводим тип Calendar к String ------------
    public String getBirthDayString() {
        String str = "";
        int day = this.birthDay.get(Calendar.DAY_OF_MONTH);
        str += ((day < 10) ? "0" : "") + day + "/";
        int mon = this.birthDay.get(Calendar.MONTH) + 1;
        str += ((mon < 10) ? "0" : "") + mon + "/";
        str += this.birthDay.get(Calendar.YEAR);
        return str;
    }

    //--- Приводим дату рождения из чисел к типу Calendar ------------
    public static Calendar makeCalendar(int day, int month, int year) {
        Calendar C = Calendar.getInstance();
        C.setTimeInMillis(0);
        C.set(Calendar.YEAR, year);
        C.set(Calendar.MONTH, month - 1);
        C.set(Calendar.DAY_OF_MONTH, day);
        return C;
    }


    @NonNull
    @Override
    public String toString() {
        return "Person{" +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", birthDay=" + birthDay +
                ", isFemale=" + isFemale +
                '}';
    }
}
