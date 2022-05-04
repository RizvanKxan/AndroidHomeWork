package com.example.listofemployees;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Person {
    private final UUID mId;
    private String firstName;
    private String secondName;
    private Calendar birthDay;
    public boolean isFemale;

    public Person() {
        this(UUID.randomUUID());
    }

    public Person(UUID id) {
        mId = id;
        birthDay = Calendar.getInstance();
    }

    public Person(String firstName, String secondName, Calendar birthDay, boolean isFemale) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDay = birthDay;
        this.isFemale = isFemale;
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
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

    public void setDate(String strDate) {
        try {
            String pattern = "dd/MM/yyyy";
            Date date = new SimpleDateFormat(pattern).parse(strDate);
            birthDay.setTime(date);
        } catch (Exception exception) {
            String ex = exception.toString();

        }
    }
}
