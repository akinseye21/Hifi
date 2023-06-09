package com.ng.hifi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays
    private int[] slides_images = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4
    };

    private String[] slide_text1 = {
            "Cover and loans for POS operators",
            "Be fraud smart",
            "Never worry about failed bank card payments",
            "Active customer support round the clock"
    };

    private String[] slide_text2 = {
            "Protect your POS business against unforeseen or natural circumstances. Also get a loan to keep your POS business afloat.",
            "Don’t fall victim to business email compromise. Verify all email payment requests.",
            "We help you make payments and purchases anytime, anywhere!.",
            "Wether you want a cover pay-out or payment processing, we resolve issues fast!."
    };

    @Override
    public int getCount() {
        return slides_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView image = view.findViewById(R.id.image);
        TextView text1 = view.findViewById(R.id.text1);
        TextView text2 = view.findViewById(R.id.text2);


        image.setImageResource(slides_images[position]);
        text1.setText(slide_text1[position]);
        text2.setText(slide_text2[position]);



        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);

    }
}
