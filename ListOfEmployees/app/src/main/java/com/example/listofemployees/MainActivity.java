package com.example.listofemployees;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IAction {

    private ArrayList<Person> personList = new ArrayList<>();
    private PersonAdapter adapter;
    public static Bitmap femaleBitmap;
    public static Bitmap maleBitmap;
    ListView listView;
    //--- Цвет фона не выбранного элемента списка ------------
    public static final int normalColor = Color.WHITE;
    //--- Цвет фона выбранного элемента списка ------------
    public static final int selectedColor = Color.YELLOW;
    //--- Индекс выбранного элемента списка ------------
    public static int curItem = -1;
    //--- Ссылка на виджет текущего выбранного элемента списка ------------
    public static View curView = null;
    //--- Название файла в котором будем хранить сотрудников
    public static final String FILE_NAME = "person.json";
    //--- Переменные для хранения информации о выделенном сотруднике, использую
    //--- для того чтобы в диалоге подтянуть информацию о сотруднике
    public static boolean isEditingDialog = false;
    public static String firstNameEditingPerson = "";
    public static String secondNameEditingPerson = "";
    public static boolean isFemaleEditingPerson = false;

    //--- Записываем список с сотрудниками в файл в виде json.
    public void savePersonsToFile(MainActivity view) {
        boolean result = JSONHelper.exportToJSON(this, personList);
        if (result) {
            Toast.makeText(this, "Данные сохранены.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Не удалось сохранить данные.", Toast.LENGTH_LONG).show();
        }
    }

    //--- Считываем объекты из файла и записываем в наш список
    public void getPersonsFromFile(MainActivity view) {
        try {
            personList = JSONHelper.importFromJSON(this);
            if (personList != null) {
                Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Не удалось открыть данные", Toast.LENGTH_LONG).show();
            }
        } catch (Exception exception) {
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //--- Проверяем есть ли файл с нашим именем и если нет, то создаём его
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            Toast.makeText(this, "Файл создан.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
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

        //--- Устанавливаем изоображения для наших Bitmap из Assets ------------
        try {
            InputStream inputStream = getAssets().open("man.png");
            InputStream inputStream2 = getAssets().open("woman.png");
            femaleBitmap = BitmapFactory.decodeStream(inputStream2);
            maleBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //--- Назначение обработчика события клика по элементу списка ------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                //--- Снимаем выделение с предыдущего выделенного ------------
                //--- элемента ------------------------------------
                if (curItem != -1) {
                    curView.setBackgroundColor(normalColor);
                }
                //--- Устанавливаем выделение на текущий элемент ------------
                //--- списка --------------------------------------
                curItem = position;
                curView = view;
                curView.setBackgroundColor(selectedColor);
                adapter.notifyDataSetChanged(); //--- если не использовать, то возникают проблемы
                //--- и предыдущее выделение не всегда стирается
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void showDialog(MenuItem item) {
        CustomDialogFragment dialog = new CustomDialogFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    //--- Добавляем меню ------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //--- Обрабатываем клики по Меню ------------
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_person:
                showDialog(item);
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
                    isEditingDialog = true;
                    Person person = adapter.getItem(curItem);
                    firstNameEditingPerson = person.getFirstName();
                    secondNameEditingPerson = person.getSecondName();
                    isFemaleEditingPerson = person.isFemale;
                    showDialog(item);


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
}