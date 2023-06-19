package com.ng.hifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> message;
    ArrayList<Boolean> isread;
    ArrayList<String> date;
    LayoutInflater inflter;

    public NotificationAdapter(Context context, ArrayList<String> message, ArrayList<Boolean> isread, ArrayList<String> date){
        this.context = context;
        this.message = message;
        this.isread = isread;
        this.date = date;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return message.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = inflter.inflate(R.layout.list_notification, null);

        TextView text1 = convertView.findViewById(R.id.text1);
        text1.setText(message.get(i));
        LinearLayout ico = convertView.findViewById(R.id.ico);
        TextView text2 = convertView.findViewById(R.id.time);
        String originalDate = date.get(i);
        String firstTenCharacters = originalDate.substring(0, Math.min(originalDate.length(), 10));
        text2.setText(firstTenCharacters);

        if (isread.get(i) == true){
            ico.setVisibility(View.GONE);
        }else if (isread.get(i) == false){
            //do nothing
        }

        return convertView;
    }
}
