package com.example.listofemployees;

import static com.example.listofemployees.MainActivity.FILE_NAME;

import android.content.Context;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONHelper {

    static boolean exportToJSON(Context context, ArrayList<Person> dataList) {

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setPersons(dataList);
        String jsonString = gson.toJson(dataItems);

        try (FileOutputStream fileOutputStream =
                     context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    static ArrayList<Person> importFromJSON(Context context) {
        try (FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
             InputStreamReader streamReader = new InputStreamReader(fileInputStream)) {

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return dataItems.getPersons();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static class DataItems {
        private ArrayList<Person> persons;

        ArrayList<Person> getPersons() {
            return persons;
        }

        void setPersons(ArrayList<Person> persons) {
            this.persons = persons;
        }
    }
}
