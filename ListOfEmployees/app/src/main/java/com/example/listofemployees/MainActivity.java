package com.example.listofemployees;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    //
    //
    //
    //
    // Больше не главный класс. В будущем будет удалён/изменён.
    //
    //
    //
    //
    //
    private ArrayList<Person> personList = new ArrayList<>();
    private static final String FILE_NAME = "person.json";
    //--- Записываем список с сотрудниками в файл в виде json.
    public void savePersonsToFile(MainActivity view) {
        boolean result = exportToJSON();
    }

    //--- Считываем объекты из файла и записываем в наш список
    public void getPersonsFromFile(MainActivity view) {
        try {
            personList = importFromJSON();
        } catch (Exception ignored) { }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //--- Если файла не существует, то создаём его ------------
        if(!getExternalPath().exists()) {
            savePersonsToFile(this);
        }

        getPersonsFromFile(this);

    }

    public File getExternalPath() {
        return new File(getExternalFilesDir(null), FILE_NAME);
    }

    boolean exportToJSON() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(personList);

        try(FileOutputStream fileOutputStream =
                    new FileOutputStream(getExternalPath())) {
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    ArrayList<Person> importFromJSON() {
        File file = getExternalPath();
        if(file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 InputStreamReader streamReader = new InputStreamReader(fileInputStream)) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Person>>(){}.getType();
                ArrayList<Person> dataItems;
                dataItems = gson.fromJson(streamReader, listType);
                return dataItems;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}