package com.ng.hifi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class LoansFragment extends Fragment {

    TextView viewLoanBalance;
    Button payBack;
    ListView listAllLoan, listPendingLoan;
    TextView txtAllLoan, txtPendingLoan;
    SharedPreferences preferences;
    String token;
    LinearLayout noLoan;
    Dialog myDialog;

    ArrayList<String> arr_RequestorName_all = new ArrayList<>();
    ArrayList<String> arr_POSName_all = new ArrayList<>();
    ArrayList<String> arr_POSAddress_all = new ArrayList<>();
    ArrayList<String> arr_Amount_all = new ArrayList<>();
    ArrayList<String> arr_POSImage_all = new ArrayList<>();
    ArrayList<String> arr_status_all = new ArrayList<>();

    ArrayList<String> arr_RequestorName_pending = new ArrayList<>();
    ArrayList<String> arr_POSName_pending = new ArrayList<>();
    ArrayList<String> arr_POSAddress_pending = new ArrayList<>();
    ArrayList<String> arr_Amount_pending = new ArrayList<>();
    ArrayList<String> arr_POSImage_pending = new ArrayList<>();

    public static final String GET_USER_LOAN = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/loans/create_get_user_loans/";

    public LoansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_loans, container, false);

        preferences = getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "not available");

        txtAllLoan = v.findViewById(R.id.allLoans);
        txtPendingLoan = v.findViewById(R.id.pendingLoans);
        listAllLoan = v.findViewById(R.id.listAllLoans);
        listPendingLoan = v.findViewById(R.id.listPendingLoans);
        noLoan = v.findViewById(R.id.noLoan);

        //show loading dialog
//        myDialog = new Dialog(getContext());
//        myDialog.setContentView(R.layout.custom_popup_loading);
//        TextView text = myDialog.findViewById(R.id.text);
//        text.setText("Loading your loan(s). Please wait...");
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.setCanceledOnTouchOutside(false);
//        myDialog.show();

        getLoan(token);

        txtAllLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPendingLoan.setVisibility(View.GONE);
                listAllLoan.setVisibility(View.VISIBLE);
                txtAllLoan.setBackgroundResource(R.drawable.corner_white);
                txtAllLoan.setTextColor(Color.parseColor("#191616"));
                txtPendingLoan.setBackgroundResource(0);
                txtPendingLoan.setTextColor(Color.parseColor("#A1A1AA"));
            }
        });
        txtPendingLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPendingLoan.setVisibility(View.VISIBLE);
                listAllLoan.setVisibility(View.GONE);
                txtAllLoan.setBackgroundResource(0);
                txtAllLoan.setTextColor(Color.parseColor("#A1A1AA"));
                txtPendingLoan.setBackgroundResource(R.drawable.corner_white);
                txtPendingLoan.setTextColor(Color.parseColor("#191616"));
            }
        });


        viewLoanBalance = v.findViewById(R.id.viewLoanBalance);
        viewLoanBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoanBalance.class));
            }
        });

        payBack = v.findViewById(R.id.payBack);
        payBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PayBackLoan.class));
            }
        });

        return v;
    }

    private void getLoan(String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_USER_LOAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();
                            if(len<1){
                                //show that there is no loan
                                listAllLoan.setVisibility(View.GONE);
                                listPendingLoan.setVisibility(View.GONE);
                                noLoan.setVisibility(View.VISIBLE);
                                payBack.setBackgroundResource(R.drawable.signup1);
                                payBack.setClickable(false);
                                payBack.setTextColor(Color.parseColor("#A1A1AA"));
                            }else{
                                //populate the loan in the listview
                                System.out.println("User loans = "+jsonArray);

                                for (int i = len - 1; i >= 0; i--) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String requestorName = jsonObject.getString("requestor_full_name");
                                    String posName = jsonObject.getString("name_of_pos_oulet");
                                    String posAddress = jsonObject.getString("address_of_pos_outlet");
                                    String posAmount = jsonObject.getString("amount_requested_for");
                                    String posImage = jsonObject.getString("pos_image");
                                    Boolean is_approved = jsonObject.getBoolean("is_approved");

                                    arr_RequestorName_all.add(requestorName);
                                    arr_POSName_all.add(posName);
                                    arr_POSAddress_all.add(posAddress);
                                    arr_Amount_all.add(posAmount);
                                    arr_POSImage_all.add(posImage);

                                    if (is_approved){
                                        //Add to array all
                                        arr_status_all.add("approved");
                                    }else{
                                        //Add to array all
                                        //Add to array pending
                                        arr_status_all.add("pending");
                                        arr_RequestorName_pending.add(requestorName);
                                        arr_POSName_pending.add(posName);
                                        arr_POSAddress_pending.add(posAddress);
                                        arr_Amount_pending.add(posAmount);
                                        arr_POSImage_pending.add(posImage);
                                    }
                                }
                            }

                            //add adapter class here
                            AllLoanAdapter allLoanAdapter = new AllLoanAdapter(getContext(), arr_RequestorName_all, arr_POSName_all, arr_POSAddress_all, arr_Amount_all, arr_POSImage_all, arr_status_all);
                            listAllLoan.setAdapter(allLoanAdapter);

                            PendingLoanAdapter pendingLoanAdapter = new PendingLoanAdapter(getContext(), arr_RequestorName_pending, arr_POSName_pending, arr_POSAddress_pending, arr_Amount_pending, arr_POSImage_pending);
                            listPendingLoan.setAdapter(pendingLoanAdapter);

//                            myDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            myDialog.dismiss();
                            Toast.makeText(getContext(), "Could not get user loans", Toast.LENGTH_SHORT).show();
                            System.out.println("Could not get user loans");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        myDialog.dismiss();
                        System.out.println("Getting loan error response = "+new String(volleyError.networkResponse.data));
                        Toast.makeText(getContext(), new String(volleyError.networkResponse.data), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

        //clear the array
        arr_RequestorName_all.clear();
        arr_POSName_all.clear();
        arr_POSAddress_all.clear();
        arr_Amount_all.clear();
        arr_POSImage_all.clear();
        arr_status_all.clear();

        arr_RequestorName_pending.clear();
        arr_POSName_pending.clear();
        arr_POSAddress_pending.clear();
        arr_Amount_pending.clear();
        arr_POSImage_pending.clear();


    }


}