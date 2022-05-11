package com.example.listofemployees.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity(tableName = "persons")
public class Person {
    @PrimaryKey()
    @NonNull
    public UUID uuid = UUID.randomUUID();
    @TypeConverters({Person.CalendarConverter.class})
    public Calendar birthDay;
    @ColumnInfo(name = "is_female")
    public boolean isFemale;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String secondName;

    public Person() {
        this(UUID.randomUUID());
    }

    public Person(UUID id) {
        uuid = id;
        birthDay = Calendar.getInstance();
    }

    public Person(String firstName, String secondName, Calendar birthDay, boolean isFemale) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDay = birthDay;
        this.isFemale = isFemale;
        uuid = UUID.randomUUID();
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

    public UUID getId() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
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

    public static class CalendarConverter {
        @TypeConverter
        public String fromCalendar(Calendar calendar) {
            String str = "";
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            str += ((day < 10) ? "0" : "") + day + "/";
            int mon = calendar.get(Calendar.MONTH) + 1;
            str += ((mon < 10) ? "0" : "") + mon + "/";
            str += calendar.get(Calendar.YEAR);
            return str;
        }

        @TypeConverter
        public Calendar toCalendar(String strDate) {
            try {
                Calendar calendar = Calendar.getInstance();
                String pattern = "dd/MM/yyyy";
                Date date = new SimpleDateFormat(pattern).parse(strDate);
                calendar.setTime(date);
                return calendar;
            } catch (Exception exception) {
                String ex = exception.toString();
                return null;
            }
        }
    }
}
