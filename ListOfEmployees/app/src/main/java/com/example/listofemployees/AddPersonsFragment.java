package com.example.listofemployees;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.UUID;

public class AddPersonsFragment extends DialogFragment {
    private RadioButton mIsFemaleRadioButton;
    private RadioButton mIsMaleRadioButton;
    private EditText mFirstNameEditText;
    private EditText mSecondNameEditText;
    private TextView mDateTextView;
    private DatePicker mDatePicker;
    private Person mPerson;
    private static Boolean mModeDialog;
    public static final String ARG_PERSON_ID = "curr_person_id";
    private IAction action;
    private View dialogView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        action = (IAction) context;
    }

    public static AddPersonsFragment newInstance(Boolean mode, UUID selectedPersonUUID) {
        mModeDialog = mode;
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_ID, selectedPersonUUID);
        AddPersonsFragment fragment = new AddPersonsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID mSelectedPersonUUID = (UUID) getArguments().getSerializable(ARG_PERSON_ID);
        mPerson = PersonBank.get(getActivity()).getPerson(mSelectedPersonUUID);
        initComponent();
    }

    private void initComponent() {
        dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_add_person, null);
        mFirstNameEditText = dialogView.findViewById(R.id.et_first_name);
        mSecondNameEditText = dialogView.findViewById(R.id.et_second_name);
        mIsFemaleRadioButton = dialogView.findViewById(R.id.isFemale);
        mIsMaleRadioButton = dialogView.findViewById(R.id.isMale);
        mDateTextView = dialogView.findViewById(R.id.tv_date_of_birt);
        mDatePicker = dialogView.findViewById(R.id.pickedDate);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String actionString = "Создать";

        if (mModeDialog) {
            actionString = "Сохранить";
            mFirstNameEditText.setText(mPerson.getFirstName());
            mSecondNameEditText.setText(mPerson.getSecondName());

            if (mPerson.isFemale) {
                mIsFemaleRadioButton.setChecked(true);
            } else {
                mIsMaleRadioButton.setChecked(true);
            }
            mDatePicker.setVisibility(View.GONE);
            mDateTextView.setVisibility(View.GONE);
        }

        return builder
                .setView(dialogView)
                .setPositiveButton(actionString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String firstName = mFirstNameEditText.getText().toString();
                        String secondName = mSecondNameEditText.getText().toString();

                        boolean isFemale;
                        isFemale = mIsFemaleRadioButton.isChecked();

                        Calendar dateOfBirth = Calendar.getInstance();
                        dateOfBirth.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());

                        //--- если имя или фамилия пусты, то выводим сообщение об этом и не создаём новый объект
                        if (secondName.trim().length() == 0 || firstName.trim().length() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Ошибка!")
                                    .setMessage("Не удалось добавить сотрудника. Имя и Фамилия должны быть заполнены.")
                                    .setPositiveButton("OK", null)
                                    .create()
                                    .show();
                        } else {
                            if (mModeDialog) {
                                action.editPerson(firstName, secondName, isFemale, mPerson.getId());
                                sendResult(Activity.RESULT_OK);
                            } else {
                                action.addPerson(firstName, secondName, isFemale, dateOfBirth);
                            }
                        }

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Отмена", null)
                .create();
    }

    //--- Отправляем ответ родителю, что мы закончили, просто чтобы обновить данные.
    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
    }
}
