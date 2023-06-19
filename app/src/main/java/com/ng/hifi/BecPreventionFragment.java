package com.ng.hifi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BecPreventionFragment extends Fragment {

    public BecPreventionFragment() {
        // Required empty public constructor
    }

    ImageView back;
    ViewPager viewPager;
    RelativeLayout buyCoverForOneOutlet, buyCoverForMultipleOutlet, requestLoan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bec_prevention, container, false);

        viewPager = getActivity().findViewById(R.id.viewpager);

        back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(0);
            }
        });

        buyCoverForOneOutlet = v.findViewById(R.id.buyCoverForOneOutlet);
        buyCoverForOneOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProtectOutlet.class);
                i.putExtra("outlet", "one outlet");
                startActivity(i);
            }
        });

        buyCoverForMultipleOutlet = v.findViewById(R.id.buyCoverForMultipleOutlet);
        buyCoverForMultipleOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProtectOutlet.class);
                i.putExtra("outlet", "multiple outlet");
                startActivity(i);
            }
        });

        requestLoan = v.findViewById(R.id.requestLoan);
        requestLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), RequestLoan2.class);
                startActivity(i);
            }
        });

        return v;
    }

    public void navigateFragment(int position){
        viewPager.setCurrentItem(position);
    }
}