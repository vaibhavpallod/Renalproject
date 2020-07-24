package com.vsp.renalproject.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vsp.renalproject.MainActivity;
import com.vsp.renalproject.R;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class Sendadapter extends RecyclerView.Adapter<Sendadapter.MyviewHolder> {

    Context context;
    List<Sendreturn> sendreturnList;
    SharedPreferences sharedPreferences;

    public Sendadapter(Context context, List<Sendreturn> sendreturnList) {
        this.context = context;
        this.sendreturnList = sendreturnList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sendrequestmodel, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, int position) {
        final Sendreturn sendreturn = sendreturnList.get(position);
        holder.mobno.setText(sendreturn.getMobno());
        holder.name.setText(sendreturn.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.requestmoneydialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();

                TextView reuestingto = dialog.findViewById(R.id.requestname);
                final EditText amount = dialog.findViewById(R.id.amountrequest);
                Button requestbtn = dialog.findViewById(R.id.requestmoneybtn);
                final EditText description = dialog.findViewById(R.id.desciptionid);
                Button cancelbtn = dialog.findViewById(R.id.cancelbtn);

                reuestingto.setText("Requesting to " + sendreturn.getName());

                requestbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(amount.getText().toString().equals("") || description.getText().toString().equals(""))) {
                            sharedPreferences = context.getSharedPreferences("SaveData", MODE_PRIVATE);
                            String name = sharedPreferences.getString("name","");
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("clientdata").child(name).child("requests").child(sendreturn.getName()).child(String.valueOf(System.currentTimeMillis()));
                            databaseReference.child("amount").setValue(amount.getText().toString());
                            databaseReference.child("description").setValue(description.getText().toString());
                            dialog.dismiss();

                            context.startActivity(new Intent(context,MainActivity.class));
                        } else {
                            toast("Fields are empty");
                        }
                    }
                });


                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.dismiss();
                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return sendreturnList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mobno;
        View view;
        Context context2;

        public MyviewHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameidmodel);
            mobno = itemView.findViewById(R.id.mobnoidmodel);
            view = itemView;
            context2 = itemView.getContext();
        }
    }

    private void toast(String x) {
        Toast.makeText(context, x, Toast.LENGTH_SHORT).show();
    }
}
