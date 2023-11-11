package com.example.employeeondemand;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RFIActivity extends AppCompatActivity {

    TextView tv_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfiactivity);

        tv_text = findViewById(R.id.tv_text);



    }
}