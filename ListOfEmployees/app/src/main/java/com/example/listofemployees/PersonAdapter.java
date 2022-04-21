package com.example.listofemployees;

import static com.example.listofemployees.MainActivity.curItem;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {

    //--- Цвет фона не выбранного элемента списка ------------
    private final int NORMAL_ITEM_COLOR = Color.WHITE;
    //--- Цвет фона выбранного элемента списка ------------
    private final int SELECTED_ITEM_COLOR = Color.YELLOW;

    public PersonAdapter(@NonNull Context context, int resource, int textViewResourceId,
                         @NonNull List<Person> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            genderImage.setImageResource(R.drawable.woman);
        } else {
            genderImage.setImageResource(R.drawable.man);
        }

        //--- Подсветка отмеченного элемента списка ------------
        //--- исключаем одновременную подсветку нескольких одинаковых элементов ------------
        if (position == curItem) {
            view.setBackgroundColor(SELECTED_ITEM_COLOR);
        } else {
            view.setBackgroundColor(NORMAL_ITEM_COLOR);
        }

        return view;
    }
}
