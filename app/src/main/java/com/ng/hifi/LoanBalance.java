package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoanBalance extends AppCompatActivity {

    ImageView back;
    LinearLayout noLoan;
    ListView listView;
    Button requestLoan;
    SharedPreferences preferences;
    String token;
    Dialog myDialog;
    TextView totalLoan;
    ImageView visibility;
    Boolean visibilityBool = true;

    ArrayList<String> arr_RequestorName = new ArrayList<>();
    ArrayList<String> arr_POSName = new ArrayList<>();
    ArrayList<String> arr_POSAddress = new ArrayList<>();
    ArrayList<String> arr_Amount = new ArrayList<>();
    ArrayList<String> arr_POSImage = new ArrayList<>();

    public static final String GET_PAID_LOANS = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/loans/get_all_loans_paid_by_user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_balance);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "not available");

        noLoan = findViewById(R.id.noLoan);
        listView = findViewById(R.id.listview);
        visibility = findViewById(R.id.visibility);
        totalLoan = findViewById(R.id.totalLoan);
        requestLoan = findViewById(R.id.requestLoan);
        requestLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoanBalance.this, RequestLoan2.class);
                startActivity(i);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visibilityBool){
                    visibilityBool = false;
                    totalLoan.setText("XXXXXX");
                    visibility.setImageResource(R.drawable.visibility_off);
                }else{
                    visibilityBool = true;
                    totalLoan.setText("300,000");
                    visibility.setImageResource(R.drawable.visibility);
                }
            }
        });

        getPaidLoans(token);
    }

    private void getPaidLoans(String token) {
        //show loading dialog
        myDialog = new Dialog(LoanBalance.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading paid loan(s). Please wait...");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_PAID_LOANS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();
                            if(len<1){
                                //show that there is no loan
                                listView.setVisibility(View.GONE);
                                noLoan.setVisibility(View.VISIBLE);
//                                requestLoan.setBackgroundResource(R.drawable.signup1);
//                                requestLoan.setClickable(false);
//                                requestLoan.setTextColor(Color.parseColor("#A1A1AA"));
                            }else{
                                //populate the loan in the listview
                                System.out.println("User paid loans = "+jsonArray);

                                for (int i = len - 1; i >= 0; i--) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String requestorName = jsonObject.getString("requestor_full_name");
                                    String posName = jsonObject.getString("name_of_pos_oulet");
                                    String posAddress = jsonObject.getString("address_of_pos_outlet");
                                    String posAmount = jsonObject.getString("amount_requested_for");
                                    String posImage = jsonObject.getString("pos_image");

                                    arr_RequestorName.add(requestorName);
                                    arr_POSName.add(posName);
                                    arr_POSAddress.add(posAddress);
                                    arr_Amount.add(posAmount);
                                    arr_POSImage.add(posImage);
                                }
                            }

                            //add adapter class here
                            PaidLoanAdapter paidLoanAdapter = new PaidLoanAdapter(LoanBalance.this, arr_RequestorName, arr_POSName, arr_POSAddress, arr_Amount, arr_POSImage);
                            listView.setAdapter(paidLoanAdapter);

                            myDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(LoanBalance.this, "Could not get paid loans", Toast.LENGTH_SHORT).show();
                            System.out.println("Could not get paid loans");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        System.out.println("Getting paid loan error response = "+new String(volleyError.networkResponse.data));
                        Toast.makeText(LoanBalance.this, new String(volleyError.networkResponse.data), Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+token);//add authentication token
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoanBalance.this);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

    }
}