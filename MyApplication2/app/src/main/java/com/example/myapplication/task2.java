package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class task2 extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConstraintLayout constraintLayout = new ConstraintLayout(this);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT , ConstraintLayout.LayoutParams.WRAP_CONTENT);

        // позиционирование по центру
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        GridLayout gridLayout = new GridLayout( this);
        gridLayout.setRowCount(3);
        gridLayout.setColumnCount(2);


        for(int i = 1; i <=4; i++){
            Button btn = new Button(this);
            btn.setText(String.valueOf(i));
            gridLayout.addView(btn);
        }

        Button btn5 = new Button(this);
        btn5.setText("Вернуться назад");
        GridLayout.LayoutParams layoutParamsBack = new GridLayout.LayoutParams();
        layoutParamsBack.columnSpec = GridLayout.spec(0,2);
        layoutParamsBack.width = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
        gridLayout.addView(btn5, layoutParamsBack);

        btn5.setOnClickListener(this);


        gridLayout.setLayoutParams(layoutParams);
        constraintLayout.addView(gridLayout);


        setContentView(constraintLayout);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
