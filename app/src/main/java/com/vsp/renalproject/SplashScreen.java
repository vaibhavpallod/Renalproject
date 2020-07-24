package com.vsp.renalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        sharedPreferences = getSharedPreferences("SaveData", MODE_PRIVATE);
        final int x = sharedPreferences.getInt("check",0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(x==0){
//                    Toast.makeText(getApplicationContext(),"gone",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    boolean case11 = sharedPreferences.getBoolean("case1", false);
//                    boolean case12 = sharedPreferences.getBoolean("case2", false);

                    if (case11) {
                        if(Backservice.serviceRunning==false){
                            startService(new Intent(SplashScreen.this,Backservice.class));
                            toast("Started");
                        }
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        },1000);

    }
    private void toast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }


}