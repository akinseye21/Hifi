package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    Timer timer;
    int Counter = 0;
    private ImageView[] mDots;
    private LinearLayout mDotLayout;
    Button createAccount, signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sliderAdapter = new SliderAdapter(MainActivity.this);

        mSlideViewPager = findViewById(R.id.slider);
        mSlideViewPager.setAdapter(sliderAdapter);
        mDotLayout = findViewById(R.id.dotsLayout2);
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });
        signIn = findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });

        //adding timer for the images
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mSlideViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        mSlideViewPager.setCurrentItem((mSlideViewPager.getCurrentItem()+1)%4);
                        Counter = Counter + 1;
                        if (Counter == 4){
                            Counter = 0;
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 8000, 8000);

        addDotIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

    }

    public void addDotIndicator(int position){

        mDots = new ImageView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i<4; i++){
            mDots[i] = new ImageView(this);
            mDots[i].setImageResource(R.drawable.view2);

            int desiredWidthInDp = 50;
            int desiredHeightInDp = 5;
            // Convert dp to pixels
            float scale = getResources().getDisplayMetrics().density;
            int desiredWidthInPx = (int) (desiredWidthInDp * scale + 0.5f);
            int desiredHeightInPx = (int) (desiredHeightInDp * scale + 0.5f);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(desiredWidthInPx, desiredHeightInPx);
            mDots[i].setLayoutParams(layoutParams);

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){

            mDots[position].setImageResource(R.drawable.view1);

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}