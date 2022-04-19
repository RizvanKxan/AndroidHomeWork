package com.example.listofemployees;

import static com.example.listofemployees.MainActivity.curItem;
import static com.example.listofemployees.MainActivity.femaleBitmap;
import static com.example.listofemployees.MainActivity.maleBitmap;
import static com.example.listofemployees.MainActivity.normalColor;
import static com.example.listofemployees.MainActivity.selectedColor;
import static com.example.listofemployees.MainActivity.curView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {

    public PersonAdapter(@NonNull Context context, int resource, int textViewResourceId,
                         @NonNull List<Person> objects) {
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
            view.setBackgroundColor(selectedColor);
            curView = view;
        } else {
            view.setBackgroundColor(normalColor);
        }

        return view;
    }
}
