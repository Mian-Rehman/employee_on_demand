package com.example.employeeondemand.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.employeeondemand.R;

public class MainActivity extends AppCompatActivity {

    ImageView introImage, introImage2;
    TextView _introText;
    CountDownTimer splashScreen;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        introImage = findViewById(R.id.introImage);
        introImage2 = findViewById(R.id.introImage2);
        _introText = findViewById(R.id._introText);

        introImage2.setScaleX(0.0f);
        introImage2.setScaleY(0.0f);
        _introText.setScaleX(0.0f);
        _introText.setScaleY(0.0f);

        introImage.animate().rotation(180f).setDuration(3000);
        introImage2.animate().scaleX(1f).setDuration(1000);
        introImage2.animate().scaleY(1f).setDuration(1000);
        _introText.animate().scaleX(1f).setDuration(1000);
        _introText.animate().scaleY(1f).setDuration(1000);

        splashScreen = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        splashScreen.start();
    }
}