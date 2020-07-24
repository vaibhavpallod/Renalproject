package com.vsp.renalproject.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vsp.renalproject.R;

import java.util.ArrayList;
import java.util.List;

public class Receivedrequest extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Receiveareturn> receiedList;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    Receiveadapter receiveadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receivedrequest_activity);
        receiedList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerreceive);
        receiveadapter = new Receiveadapter(Receivedrequest.this, receiedList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Drawable background = getResources().getDrawable(R.drawable.gradient_actionbar);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));
        getSupportActionBar().setTitle("Receieved Requests");
        sharedPreferences = getSharedPreferences("SaveData", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");


        databaseReference = FirebaseDatabase.getInstance().getReference().child("clientdata").child(name).child("requests");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot snapshot1, @Nullable String previousChildName) {
                snapshot1.getRef().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.child("amount").exists() && snapshot.child("description").exists()) {

                        receiedList.add(new Receiveareturn(snapshot1.getKey(), snapshot.child("amount").getValue().toString(), snapshot.child("description").getValue().toString(), snapshot.getKey()));
                        receiveadapter.notifyDataSetChanged();
                    }
                }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(receiveadapter);
        receiveadapter.notifyDataSetChanged();


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}