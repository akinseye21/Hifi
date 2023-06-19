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
    String passedEmail, passedUsername, passedFullName, passedPhoneNumber, from;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        passedEmail = preferences.getString("email", "");
        passedUsername = preferences.getString("username", "");
        passedFullName = preferences.getString("fullname", "");
        passedPhoneNumber = preferences.getString("phonenumber", "");
        Intent i = getIntent();
        from = i.getStringExtra("from");


        //view pager and tab layout
        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        if (from.equals("RequestLoan")){
            navigateFragment(1);
        }
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

    public void navigateFragment(int position){
        viewPager.setCurrentItem(position);
    }
}