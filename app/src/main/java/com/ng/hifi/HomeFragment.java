package com.ng.hifi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    RelativeLayout protectPOSOutlet, becFraudPrevention;
    SharedPreferences preferences;
    String username, referralCode;
    TextView name;
    CircleImageView profilePic;
    ViewPager viewPager;
    ImageView notificationIMG;

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
        referralCode = preferences.getString("referralCode", "not available");

        viewPager = getActivity().findViewById(R.id.viewpager);
        notificationIMG = v.findViewById(R.id.notification);
        notificationIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Notifications.class));
            }
        });
        profilePic = v.findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSideMenu();
            }
        });
        name = v.findViewById(R.id.username);
        name.setText("Hello "+username);
        protectPOSOutlet = v.findViewById(R.id.protectPOSOutlet);
        protectPOSOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to bar index 1
                navigateFragment(1);
            }
        });
        becFraudPrevention = v.findViewById(R.id.becFraudPrevention);
        becFraudPrevention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BecFraudPrevention.class));
            }
        });

        return v;
    }

    public void openSideMenu() {

        HomeScreen homeScreen = (HomeScreen) getActivity();

        if (homeScreen != null) {
            // Find the view in the activity
            DrawerLayout drawerLayout;
            LinearLayout drawerItemsLayout;
            View overlayView;

            //drawer menu bar
            drawerItemsLayout = homeScreen.findViewById(R.id.drawer_items_layout);
            overlayView = homeScreen.findViewById(R.id.overlayView);
            drawerLayout = homeScreen.findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            overlayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close the drawer when the overlay is clicked
                    drawerLayout.closeDrawers();
                }
            });

            drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerOpened(View drawerView) {
                    overlayView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    overlayView.setVisibility(View.GONE);
                }
            });

            // Inflate the drawer item layout
            View drawerItem = getLayoutInflater().inflate(R.layout.drawer_item, drawerItemsLayout, false);

            ImageView back = drawerItem.findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });
            TextView personName = drawerItem.findViewById(R.id.username);
            TextView refCode = drawerItem.findViewById(R.id.referralCode);
            RelativeLayout myLoans = drawerItem.findViewById(R.id.myLoan);
            RelativeLayout security = drawerItem.findViewById(R.id.security);
            RelativeLayout contactUs = drawerItem.findViewById(R.id.contactUs);
            RelativeLayout termsAndCondition = drawerItem.findViewById(R.id.termsAndConditions);
            Button logout =  drawerItem.findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog myDialog = new Dialog(getContext());
                    myDialog.setContentView(R.layout.custom_popup_logout);
                    Button yes = myDialog.findViewById(R.id.yes);
                    Button no = myDialog.findViewById(R.id.no);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //clear shared preference
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            //go to main activity page
                            Intent i = new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                        }
                    });
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();
                }
            });
            personName.setText(username);
            refCode.setText(referralCode);
            myLoans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), Loans.class));
                }
            });
            // Add the item to the drawer layout
            drawerItemsLayout.addView(drawerItem);
        }

    }

    public void navigateFragment(int position){
        viewPager.setCurrentItem(position);
    }

}