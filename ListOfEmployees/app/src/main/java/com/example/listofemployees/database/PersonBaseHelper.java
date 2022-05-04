package com.example.listofemployees.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.listofemployees.database.PersonDbScheme.PersonTable;

public class PersonBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "personBase.db";

    public PersonBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PersonTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PersonTable.Cols.UUID + ", " +
                PersonTable.Cols.FIRST_NAME + ", " +
                PersonTable.Cols.SECOND_NAME + ", " +
                PersonTable.Cols.BIRTHDAY + ", " +
                PersonTable.Cols.FEMALE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
