package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.criminalintent.database.CrimeDbScheme.CrimeTable;

/*
При вызове метода new CrimeBaseHelper(Context).getWritableDatabase() будет
выполнено следующее:
- Открывает файл "crimeBase.db" если он существует, если нет, то создаёт его
- Если база открывается впервые, то вызывает метод onCreate(SQLiteDatabase)
с последующим сохранением последнего номера версии
- Если база открывается не впервые, проверяет номер её версии и если версия
в CrimeBaseHelper выше, то вызывает метод onUpgrade().
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + ", " +
                CrimeTable.Cols.TITLE + ", " +
                CrimeTable.Cols.DATE + ", " +
                CrimeTable.Cols.SOLVED + ")"
        );
    };

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
