package com.vsp.renalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsp.renalproject.History.Bottomhistory;
import com.vsp.renalproject.home.Bottomhome;

import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private int check;
    private String repeatname;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("SaveData", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentid, new Bottomhome()).commit();
        }
        getSupportActionBar().setTitle("Renal's Payment App");
        Drawable background = getResources().getDrawable(R.drawable.gradient_actionbar);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavid);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment selectedfragment = null;

        switch (item.getItemId()) {
            case R.id.bottomhomeid:
                selectedfragment = new Bottomhome();
                break;

            case R.id.bottomhistoryid:
                selectedfragment = new Bottomhistory();
                Toast.makeText(getApplicationContext(), "Profile fragment not added due to lack of time ", Toast.LENGTH_SHORT).show();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentid, selectedfragment).commit();


        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){

            case R.id.signoutfromdrawer:
                SharedPreferences sharedPreferences;
                SharedPreferences.Editor editor;
                sharedPreferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);

                FirebaseAuth.getInstance().signOut();
                editor = sharedPreferences.edit();
                editor.putBoolean("case1", false);
                editor.putBoolean("case2", false);
                editor.apply();
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {


        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();


    }

    private void toast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }
}