package com.vsp.renalproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Signupform extends AppCompatActivity {
    int check, total = 0;
    ArrayList<String> mobnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupform);
        check = 0;
        mobnos = new ArrayList<>();

        Button button = findViewById(R.id.signupbtn);
        final EditText name, email, pass, confpass, mobno;
        mobno = findViewById(R.id.mobnoinsignupform);
        name = findViewById(R.id.nameinsignupform);
        email = findViewById(R.id.emailinsignupform);
        pass = findViewById(R.id.passwordinsignupform);
        confpass = findViewById(R.id.confirmpasswordinsignupform);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Drawable background = getResources().getDrawable(R.drawable.gradient_actionbar);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));
        getSupportActionBar().setTitle("Signup Form");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("mobnocheck")) {
                    total = Integer.valueOf(String.valueOf(snapshot.getChildrenCount()));

                    snapshot.getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            mobnos.add(snapshot.getKey());
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("") || email.getText().toString().equals("") || mobno.getText().toString().equals("") || pass.getText().toString().equals("") || confpass.getText().toString().equals("")
                        || !confpass.getText().toString().equals(pass.getText().toString()) || pass.getText().length() < 9 || mobno.getText().length() != 10) {
                    if (name.getText().toString().equals(""))
                        name.setError("This field can't be empty");
                    if (mobno.getText().toString().equals(""))
                        mobno.setError("This field can't be empty");
                    if (email.getText().toString().equals(""))
                        email.setError("This field can't be empty");
                    if (pass.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Password field can't be empty", Toast.LENGTH_LONG).show();
                    if (confpass.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Password field can't be empty", Toast.LENGTH_LONG).show();

                    if (mobno.getText().length() != 10)
                        mobno.setError("Mobile number should be 10 digit");
                    if (!confpass.getText().toString().equals(pass.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Password doesnot match", Toast.LENGTH_LONG).show();
                    if (pass.getText().length() < 9)
                        Toast.makeText(getApplicationContext(), "Password length cannot be less than 8 alphabets", Toast.LENGTH_LONG).show();


                } else {
                    for (int i = 0; i < mobnos.size(); i++) {

                        Log.i("mobnosssignup", mobnos.get(i));
                        if (mobno.getText().toString().equals(mobnos.get(i))) {
                            Toast.makeText(getApplicationContext(), "This mobile number is already registered us please read Instructions", Toast.LENGTH_LONG).show();
                            i = mobnos.size();
                        } else if (i == mobnos.size() - 1) {
                            Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_LONG).show();
                            String username = name.getText().toString();
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("mobnocheck").child(mobno.getText().toString()).setValue(email.getText().toString());
                            databaseReference.child("check").child(username).setValue(email.getText().toString());
                            databaseReference.child("clientdata").child(username).child("mydetails").child("mobno").setValue(mobno.getText().toString());
                            databaseReference.child("clientdata").child(username).child("mydetails").child("email").setValue(email.getText().toString());

                            Intent intent = new Intent(Signupform.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }


            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
