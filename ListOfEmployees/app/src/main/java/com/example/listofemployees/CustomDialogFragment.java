package com.example.listofemployees;


import static com.example.listofemployees.MainActivity.firstNameEditingPerson;
import static com.example.listofemployees.MainActivity.isEditingDialog;
import static com.example.listofemployees.MainActivity.isFemaleEditingPerson;
import static com.example.listofemployees.MainActivity.secondNameEditingPerson;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class CustomDialogFragment extends DialogFragment {

    private IAction action;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action = (IAction) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String actionString = "Добавить";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_person, null);

        //--- Если находимся в режиме редактирования, то устанавливаем значения выделенного сотрудника
        //--- в элементы диалога, а изменение даты скрываем
        if(isEditingDialog) {
            actionString = "Сохранить";
            ((EditText) dialogView.findViewById(R.id.et_first_name)).setText(firstNameEditingPerson);
            ((EditText) dialogView.findViewById(R.id.et_second_name)).setText(secondNameEditingPerson);

            if(isFemaleEditingPerson) {
                ((RadioButton) dialogView.findViewById(R.id.isFemale)).setChecked(true);
            } else {
                ((RadioButton) dialogView.findViewById(R.id.isMale)).setChecked(true);
            }
            ((DatePicker) dialogView.findViewById(R.id.pickedDate)).setVisibility(View.GONE);
            ((TextView) dialogView.findViewById(R.id.tv_date_of_birt)).setVisibility(View.GONE);

            //--- выходим из режима редактирования
            isEditingDialog = false;
        }

        return builder
                .setView(dialogView)
                .setPositiveButton(actionString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText temp = dialogView.findViewById(R.id.et_second_name);
                        String secondName = temp.getText().toString();

                        temp = dialogView.findViewById(R.id.et_first_name);
                        String firstName = temp.getText().toString();

                        RadioButton femaleRadioButton = dialogView.findViewById(R.id.isFemale);
                        boolean isFemale;
                        if (femaleRadioButton.isChecked()) {
                            isFemale = true;
                        } else {
                            isFemale = false;
                        }

                        DatePicker datePicker = dialogView.findViewById(R.id.pickedDate);
                        Calendar dateOfBirth = Calendar.getInstance();
                        dateOfBirth.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                        //--- если имя или фамилия пусты, то выводим сообщение об этом и не создаём новый объект
                        if (secondName.trim().length() == 0 || firstName.trim().length() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Ошибка!")
                                    .setMessage("Не удалось добавить сотрудника. Имя и Фамилия должны быть заполнены.")
                                    .setPositiveButton("OK", null)
                                    .create()
                                    .show();
                        } else {
                            action.addPerson(firstName, secondName, isFemale, dateOfBirth);
                        }

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }
}

