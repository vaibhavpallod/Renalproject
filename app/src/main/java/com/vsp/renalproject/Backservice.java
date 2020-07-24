package com.vsp.renalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Backservice extends Service {
    private SharedPreferences sharedPreferences;
    private String repeatname;
    public static Boolean serviceRunning = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = getSharedPreferences("SaveData", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        serviceRunning = true;

        repeatname = "";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("clientdata").child(name);
 /* String amount =snapshot.child("amount").getValue().toString();
                            String descrip = snapshot.child("description").getValue().toString();
                            toast(amount);
                            toast(descrip);
*/
        databaseReference.child("requests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                toast(snapshot.getKey());
//                toast("New Child added");
                /*toast("New request added");
                toast("Please request again to check amount and name toast");*/

                if (snapshot.getChildrenCount() == 1) {
                    toast("Please request again something went wrong ");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                  /*  toast("PRE  " + previousChildName);
                    Log.i("detailss", previousChildName);
*/
                if (!repeatname.equals(snapshot.getKey())) {
                    repeatname = snapshot.getKey();
                    snapshot.getRef().limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                          toast(snapshot.getRef().limitToLast(1).getRef().getKey());
                            String amount = "novalue", descrip = "nodescip";
                            if (snapshot.child("amount").exists() && snapshot.child("description").exists()) {
                                amount = snapshot.child("amount").getValue().toString();
                                descrip = snapshot.child("description").getValue().toString();
                                toast("NEW request detected with amount "+amount+" & description with "+descrip);
                               /* Intent intent = new Intent(getApplicationContext(), sendnotification.class);
                                intent.putExtra("nametonoti", repeatname);
                                startActivity(intent);*/
//                                sendnoti(repeatname);
//                                toast(descrip);
                            } else
                                repeatname = "";
                            Log.e("detailss", amount);
                            Log.e("detailss", descrip);


                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                                previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String
                                previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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


        return START_STICKY;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceRunning = false;
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }

    private void toast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_LONG).show();
    }
}
