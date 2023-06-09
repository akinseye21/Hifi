package com.ng.hifi;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreen extends AppCompatActivity {

    private TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout drawerItemsLayout;
    ImageView menu;
    DrawerLayout drawerLayout;
    String passedEmail, passedUsername, passedFullName, passedPhoneNumber;
    private View overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        passedEmail = i.getStringExtra("email");
        passedUsername = i.getStringExtra("username");
        passedFullName = i.getStringExtra("fullname");
        passedPhoneNumber = i.getStringExtra("phonenumber");

        //drawer menu bar
        overlayView = findViewById(R.id.overlayView);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

        menu = findViewById(R.id.menu);
        drawerItemsLayout = findViewById(R.id.drawer_items_layout);
        createDrawerItem();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //view pager and tab layout
        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void createDrawerItem() {
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
        RelativeLayout myLoans = drawerItem.findViewById(R.id.myLoan);
        RelativeLayout security = drawerItem.findViewById(R.id.security);
        RelativeLayout contactUs = drawerItem.findViewById(R.id.contactUs);
        RelativeLayout termsAndCondition = drawerItem.findViewById(R.id.termsAndConditions);

        personName.setText(passedUsername);
        myLoans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, Loans.class));
            }
        });


        // Add the item to the drawer layout
        drawerItemsLayout.addView(drawerItem);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.bec_prevention);
        tabLayout.getTabAt(2).setIcon(R.drawable.loan);
        tabLayout.getTabAt(3).setIcon(R.drawable.online_payment);
    }

    private void addTabs(ViewPager viewPager) {
        HomeScreen.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "Home");
        adapter.addFrag(new BecPreventionFragment(), "BEC Prevention");
        adapter.addFrag(new LoansFragment(), "Loans");
        adapter.addFrag(new OnlinePaymentFragment(), "Online Payment");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}