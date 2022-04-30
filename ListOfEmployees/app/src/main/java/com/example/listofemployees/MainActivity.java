package com.example.listofemployees;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements IAction {

    private ArrayList<Person> personList = new ArrayList<>();
    private PersonAdapter adapter;
    ListView listView;
    //--- Индекс выбранного элемента списка ------------
    public static int curItem = -1;
    //--- Название файла в котором будем хранить сотрудников
    private static final String FILE_NAME = "person.json";
    //--- Переменные для хранения информации о выделенном сотруднике, использую
    //--- для того чтобы в диалоге подтянуть информацию о сотруднике
    public static String firstNameEditingPerson = "";
    public static String secondNameEditingPerson = "";
    public static boolean isFemaleEditingPerson = false;

    //--- Записываем список с сотрудниками в файл в виде json.
    public void savePersonsToFile(MainActivity view) {
        boolean result = exportToJSON();
        if (result) {
            Toast.makeText(this, "Данные сохранены.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Не удалось сохранить данные.", Toast.LENGTH_LONG).show();
        }
    }

    //--- Считываем объекты из файла и записываем в наш список
    public void getPersonsFromFile(MainActivity view) {
        try {
            personList = importFromJSON();
            if (personList != null) {
                Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Не удалось открыть данные", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ignored) { }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--- Если файла не существует, то создаём его ------------
        if(!getExternalPath().exists()) {
            savePersonsToFile(this);
        }

        getPersonsFromFile(this);

        listView = findViewById(R.id.list_view);
        adapter = new PersonAdapter(
                this,
                R.layout.item_person,
                R.id.tv_name,
                personList
        );
        listView.setAdapter(adapter);

        //--- Назначение обработчика события клика по элементу списка ------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                //--- Устанавливаем выделение на текущий элемент списка ------------
                curItem = position;
                adapter.notifyDataSetChanged(); //--- если не использовать, то возникают проблемы
                //--- и предыдущее выделение не всегда стирается ------------
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePersonsToFile(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePersonsToFile(this);
    }

    @Override
    public void addPerson(String firstName, String secondName, boolean isFemale, Calendar dateOfBirth) {
        Person person = new Person(firstName,secondName,dateOfBirth,isFemale);
        adapter.add(person);
        adapter.notifyDataSetChanged(); //<--- без этого ListView не синхронизирует отоображение ------------
        ///--- и даже можно будет словить exception ------------
    }

    @Override
    public void editPerson(String firstName, String secondName, boolean isFemale) {
        Person person;
        if(curItem != -1) {
            person = adapter.getItem(curItem);
            person.setFirstName(firstName);
            person.setSecondName(secondName);
            person.setFemale(isFemale);
            adapter.notifyDataSetChanged();
        }
    }

    //--- Обрабатываем клики по Меню ------------
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_person:
                //showDialog();
                return true;
            case R.id.remove_person:
                if (!adapter.isEmpty() && curItem != -1) {
                    Person person = adapter.getItem(curItem);
                    adapter.remove(person);
                    curItem = curItem - 1;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Список пуст или ничего не выбрано!")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
                return true;
            case R.id.edit_person:

                if (!adapter.isEmpty() && curItem != -1) {
                   // isEditingDialog = true;
                    Person person = adapter.getItem(curItem);
                    firstNameEditingPerson = person.getFirstName();
                    secondNameEditingPerson = person.getSecondName();
                    isFemaleEditingPerson = person.isFemale;
                    //showDialog(item);


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Внимание!")
                            .setMessage("Список пуст или ничего не выбрано!")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
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