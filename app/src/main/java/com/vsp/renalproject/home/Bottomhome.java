package com.vsp.renalproject.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vsp.renalproject.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Bottomhome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmenthome, container, false);

        ImageView sendrequest = view.findViewById(R.id.sendrequestid);
        ImageView receiverequest = view.findViewById(R.id.receivedrequest);
        ImageView history = view.findViewById(R.id.historyimgid);


        sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Sendrequest.class);
                startActivity(intent);
            }
        });

        receiverequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Receivedrequest.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "History activity not added due to lack of time ", Toast.LENGTH_SHORT).show();

            }
        });
        return view;


    }
}
