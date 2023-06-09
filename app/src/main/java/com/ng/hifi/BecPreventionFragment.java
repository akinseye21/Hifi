package com.ng.hifi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class BecPreventionFragment extends Fragment {

    public BecPreventionFragment() {
        // Required empty public constructor
    }

    RelativeLayout buyCoverForOneOutlet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bec_prevention, container, false);

        buyCoverForOneOutlet = v.findViewById(R.id.buyCoverForOneOutlet);
        buyCoverForOneOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProtectOutlet.class);
                startActivity(i);
            }
        });

        return v;
    }
}