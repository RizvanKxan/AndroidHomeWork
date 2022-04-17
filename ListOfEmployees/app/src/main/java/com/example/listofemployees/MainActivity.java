package com.example.listofemployees;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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
    private final int normalColor = Color.WHITE;
    //--- Цвет фона выбранного элемента списка ------------
    private final int selectedColor = Color.YELLOW;
    //--- Индекс выбранного элемента списка ------------
    private static int curItem = -1;
    //--- Ссылка на виджет текущего выбранного элемента списка ------------
    private View curView = null;
    //--- Название файла в котором будем хранить сотрудников
    private static final String FILE_NAME = "person.json";
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
                    MainActivity.this.curView.setBackgroundColor(MainActivity.this.normalColor);
                }
                //--- Устанавливаем выделение на текущий элемент ------------
                //--- списка --------------------------------------
                curItem = position;
                MainActivity.this.curView = view;
                MainActivity.this.curView.setBackgroundColor(MainActivity.this.selectedColor);
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
        personList.add(new Person(firstName, secondName, dateOfBirth, isFemale));
        adapter.notifyDataSetChanged(); //<--- без этого ListView не синхронизирует отоображение ------------
        ///--- и даже можно будет словить exception ------------
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
                    firstNameEditingPerson = person.firstName;
                    secondNameEditingPerson = person.secondName;
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

    private final class PersonAdapter extends ArrayAdapter<Person> {

        public PersonAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Person> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            Person person = getItem(position);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvSecondName = view.findViewById(R.id.tv_second_name);
            TextView tvDateOfBirt = view.findViewById(R.id.tv_date_of_birt);
            ImageView genderImage = view.findViewById(R.id.image_gender);

            tvName.setText(person.getFirstName());
            tvSecondName.setText(person.getSecondName());
            tvDateOfBirt.setText(person.getBirthDayString());

            //--- Устанавливаем картинку в зависимости от пола человека ------------
            if (person.isFemale) {
                genderImage.setImageBitmap(femaleBitmap);
            } else {
                genderImage.setImageBitmap(maleBitmap);
            }

            //--- Подсветка отмеченного элемента списка ------------
            //--- исключаем одновременную подсветку нескольких одинаковых элементов ------------
            if (position == curItem) {
                view.setBackgroundColor(MainActivity.this.selectedColor);
                MainActivity.this.curView = view;
            } else {
                view.setBackgroundColor(MainActivity.this.normalColor);
            }

            return view;
        }
    }


    public static class Person {
        private int personID = 0;
        private final String firstName;
        private final String secondName;
        private final Calendar birthDay;
        public boolean isFemale;

        public Person(String firstName, String secondName, Calendar birthDay, boolean isFemale) {
            this.firstName = firstName;
            this.secondName = secondName;
            this.birthDay = birthDay;
            this.isFemale = isFemale;
            personID = personID++;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
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
                    "personID=" + personID +
                    ", firstName='" + firstName + '\'' +
                    ", secondName='" + secondName + '\'' +
                    ", birthDay=" + birthDay +
                    ", isFemale=" + isFemale +
                    '}';
        }
    }

    static class JSONHelper {

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
}