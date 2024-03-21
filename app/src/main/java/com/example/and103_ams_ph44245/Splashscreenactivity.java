package com.example.and103_ams_ph44245;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class Splashscreenactivity extends AppCompatActivity {
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreenactivity);

        lottie = findViewById(R.id.lottie);
        lottie.animate().translationY(2000).setDuration(2700).setStartDelay(2900);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreenactivity.this, Login.class);
                startActivity(intent);
            }
        }, 3000);
    }
}