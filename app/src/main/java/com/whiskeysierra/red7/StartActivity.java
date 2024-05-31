package com.whiskeysierra.red7;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StartActivity extends AppCompatActivity {

    Button button_minus, button_plus, button_start, button_rules, button_back;
    TextView textView_numOfPlayers;
    private ViewPager viewPager;
    private PdfRenderer pdfRenderer;
    private PdfPagerAdapter pdfPagerAdapter;
    private int pageCount;
    View popupView;
    int numOfPlayers = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button_minus            = findViewById(R.id.button_minus);
        button_plus             = findViewById(R.id.button_plus);
        button_start            = findViewById(R.id.button_start);
        button_rules            = findViewById(R.id.button_rules);
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

        button_rules.setOnClickListener((v -> openRules()));
    }

    protected void openRules() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        popupView = inflater.inflate(R.layout.rules_popup_layout, null);

        button_back = popupView.findViewById(R.id.button_back);
        viewPager   = popupView.findViewById(R.id.viewPager);

        try {
            File file = new File(getCacheDir(), "rules.pdf");
            pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            pageCount = pdfRenderer.getPageCount();

            // Set up the ViewPager with the PdfPagerAdapter
            pdfPagerAdapter = new PdfPagerAdapter(this, pdfRenderer, pageCount);
            viewPager.setAdapter(pdfPagerAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
    }
}