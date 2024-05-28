package com.whiskeysierra.red7;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

public class StartActivity extends AppCompatActivity {

    Button button_minus, button_plus, button_start;
    TextView textView_numOfPlayers;
    int numOfPlayers = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button_minus            = findViewById(R.id.button_minus);
        button_plus             = findViewById(R.id.button_plus);
        button_start            = findViewById(R.id.button_start);
        textView_numOfPlayers   = findViewById(R.id.textView_numOfPlayers);

        textView_numOfPlayers.setText(String.valueOf(numOfPlayers));


        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOfPlayers > 2) textView_numOfPlayers.setText(String.valueOf(--numOfPlayers));
            }
        });

        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOfPlayers < 4) textView_numOfPlayers.setText(String.valueOf(++numOfPlayers));
            }
        });

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("numOfPlayers", numOfPlayers);
                startActivity(intent);
            }
        });

    }
}