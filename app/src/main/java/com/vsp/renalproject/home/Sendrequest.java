package com.vsp.renalproject.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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

public class Sendrequest extends AppCompatActivity {
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    List<Sendreturn> sendreturnList;
    Sendadapter sendadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendrequest_activity);
        sendreturnList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewsendrequest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Drawable background = getResources().getDrawable(R.drawable.gradient_actionbar);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));
        getSupportActionBar().setTitle("Signup Form");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sendadapter = new Sendadapter(Sendrequest.this,sendreturnList);
        databaseReference.child("clientdata").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String mobilenumber = snapshot.child("mydetails").child("mobno").getValue().toString();
                String name = snapshot.getKey();

                sendreturnList.add(new Sendreturn(name,mobilenumber));
                recyclerView.setAdapter(sendadapter);
                sendadapter.notifyDataSetChanged();

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}