package com.ng.hifi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    RelativeLayout becFraudPrevention;
    SharedPreferences preferences;
    String username;
    TextView name;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        preferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        username = preferences.getString("username", "not available");

        name = v.findViewById(R.id.username);
        name.setText("Hello "+username);
        becFraudPrevention = v.findViewById(R.id.becFraudPrevention);
        becFraudPrevention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BecFraudPrevention.class));
            }
        });

        return v;
    }
}