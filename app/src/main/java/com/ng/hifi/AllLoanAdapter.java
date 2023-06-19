package com.ng.hifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllLoanAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> arr_requestorName;
    ArrayList<String> arr_POSName;
    ArrayList<String> arr_POSAddress;
    ArrayList<String> arr_POSAmount;
    ArrayList<String> arr_POSImage;
    ArrayList<String> arr_status;
    LayoutInflater inflter;

    public AllLoanAdapter(Context context, ArrayList<String> arr_requestorName, ArrayList<String> arr_POSName, ArrayList<String> arr_POSAddress, ArrayList<String> arr_POSAmount, ArrayList<String> arr_POSImage, ArrayList<String> arr_status){
        this.context = context;
        this.arr_requestorName = arr_requestorName;
        this.arr_POSName = arr_POSName;
        this.arr_POSAddress = arr_POSAddress;
        this.arr_POSAmount = arr_POSAmount;
        this.arr_POSImage = arr_POSImage;
        this.arr_status = arr_status;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arr_requestorName.size();
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
        convertView = inflter.inflate(R.layout.list_all_loans, null);

        TextView text1 = convertView.findViewById(R.id.text1);
        text1.setText("Your loan request of ₦"+arr_POSAmount.get(i));
        ImageView ico = convertView.findViewById(R.id.ico);
        TextView text2 = convertView.findViewById(R.id.text2);



        if (arr_status.get(i).equals("approved")){
            //do nothing
        }else if (arr_status.get(i).equals("pending")){
//            ico.setImageResource(R.drawable.pending_loan);
            text2.setText("is still pending");
        }

        return convertView;
    }
}
