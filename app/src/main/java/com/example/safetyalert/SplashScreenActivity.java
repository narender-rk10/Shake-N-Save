package com.example.safetyalert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                if(!preferences.getBoolean("onboarding_complete",false)){
                    Intent i=new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
                    startActivity(i);
                    finish();
                    return;
                }
                else
                {
                    String userphonenumber = sh.getString("phone", null);
                    if (userphonenumber!=null) {
                        Intent i = new Intent(SplashScreenActivity.this, Home.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreenActivity.this, LoginScreen.class);
                        startActivity(i);
                        finish();
                    }

                }

            }
        }, 5000);
    }
    }
