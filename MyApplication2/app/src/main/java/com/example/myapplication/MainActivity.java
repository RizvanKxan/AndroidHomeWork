package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView JavaTextView;
    private static final String JAVA_VISIBILITY = "JAVA_VISIBILITY";
    TextView AndroidTextView;
    private static final String ANDROID_VISIBILITY = "ANDROID_VISIBILITY";
    TextView XMLTextView;
    private static final String XML_VISIBILITY = "XML_VISIBILITY";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(JAVA_VISIBILITY, JavaTextView.getVisibility());
        outState.putInt(ANDROID_VISIBILITY, AndroidTextView.getVisibility());
        outState.putInt(XML_VISIBILITY, XMLTextView.getVisibility());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        JavaTextView.setVisibility(savedInstanceState.getInt(JAVA_VISIBILITY));
        AndroidTextView.setVisibility(savedInstanceState.getInt(ANDROID_VISIBILITY));
        XMLTextView.setVisibility(savedInstanceState.getInt(XML_VISIBILITY));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JavaTextView = findViewById(R.id.JavaTextView);
        AndroidTextView = findViewById(R.id.AndroidTextView);
        XMLTextView = findViewById(R.id.XMLTextView);
    }

    public void onCheckboxClicked(View view) {
        // Получаем флажок
        CheckBox checkBox = (CheckBox) view;
        // Получаем, отмечен ли данный флажок
        boolean checked = checkBox.isChecked();

        // Смотрим, какой именно из флажков отмечен
        switch(view.getId()) {
            case R.id.JavaCheckBox:
                TextView JavaTextView = findViewById(R.id.JavaTextView);
                if (checked) {
                    JavaTextView.setVisibility(View.VISIBLE);
                } else {
                    JavaTextView.setVisibility(View.GONE);
                }
                break;
            case R.id.AndroidCheckBox:
                TextView AndroidTextView = findViewById(R.id.AndroidTextView);
                if (checked) {
                    AndroidTextView.setVisibility(View.VISIBLE);
                } else {
                    AndroidTextView.setVisibility(View.GONE);
                }
                break;
            case R.id.XMLCheckBox:
                TextView XMLTextView = findViewById(R.id.XMLTextView);
                if (checked) {
                    XMLTextView.setVisibility(View.VISIBLE);
                } else {
                    XMLTextView.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void buttonClick(View view) {
        Intent intent = new Intent(this, task2.class);
        startActivity(intent);
    }
}