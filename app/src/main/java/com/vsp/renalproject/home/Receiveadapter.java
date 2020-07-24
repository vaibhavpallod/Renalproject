package com.vsp.renalproject.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vsp.renalproject.Googlepaybtn;
import com.vsp.renalproject.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Receiveadapter extends RecyclerView.Adapter<Receiveadapter.MyviewHolder> {
    Context context;
    List<Receiveareturn> receiveareturnList;

    public Receiveadapter(Context context, List<Receiveareturn> receiveareturnList) {
        this.context = context;
        this.receiveareturnList = receiveareturnList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.receivedmodel, parent, false);


        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.name.setText(receiveareturnList.get(position).getName());
        holder.amount.setText(String.valueOf(receiveareturnList.get(position).getAmount()));
        holder.paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Googlepaybtn.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiveareturnList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        Button paybtn;
        TextView name, amount;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namereceivemodel);
            amount = itemView.findViewById(R.id.amountreceivemodel);
            paybtn = itemView.findViewById(R.id.paybtnid);
          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.requestmoneydialog);


                    dialog.show();


                }
            });
*/

        }
    }
}
