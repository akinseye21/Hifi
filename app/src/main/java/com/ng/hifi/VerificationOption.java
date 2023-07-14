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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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

import java.util.HashMap;
import java.util.Map;

public class VerificationOption extends AppCompatActivity {

    ImageView back;
    RelativeLayout emailAddress, phoneNumber;
    RadioButton email, phone;
    Button verifyAccount;
    String selected = "";
    String fullName, s_phoneNumber, s_emailAddress, username, password;
    Dialog myDialog;
    Boolean userEmailSignUp;

    public static final String EMAIL_VERIFICATION = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/activate_account/";
    public static final String PHONE_VERIFICATION = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/request_user_phone_number_verification/";

    public static final String SIGN_UP = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/register/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_option);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        fullName = i.getStringExtra("fullname");
        s_phoneNumber = i.getStringExtra("phone");
        s_emailAddress = i.getStringExtra("email");
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VerificationOption.this, CreateAccount.class));
            }
        });

        emailAddress = findViewById(R.id.emailAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.radioEmail);
        phone = findViewById(R.id.radioPhone);
        verifyAccount = findViewById(R.id.continued);

        emailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress.setBackgroundResource(R.drawable.card2);
                email.setChecked(true);
                phoneNumber.setBackgroundResource(R.drawable.card1);
                phone.setChecked(false);
                selected = "email";
                userEmailSignUp = true;
            }
        });
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress.setBackgroundResource(R.drawable.card1);
                email.setChecked(false);
                phoneNumber.setBackgroundResource(R.drawable.card2);
                phone.setChecked(true);
                selected = "phone";
                userEmailSignUp = false;
            }
        });

        verifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected.equals("phone")){
                    //show dialog loading
                    myDialog = new Dialog(VerificationOption.this);
                    myDialog.setContentView(R.layout.custom_popup_loading);
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Verifying phone number...\nSigning you up, Please wait");
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();
                    SignUpUser();
                }else if (selected.equals("email")){
                    //show dialog loading
                    myDialog = new Dialog(VerificationOption.this);
                    myDialog.setContentView(R.layout.custom_popup_loading);
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Verifying Email...\nSigning you up, Please wait");
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();
                    SignUpUser();
                }else if (selected.equals("")){
                    Toast.makeText(VerificationOption.this, "Please select a means of verification", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SignUpUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,SIGN_UP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        myDialog.dismiss();

                        Intent i = new Intent(VerificationOption.this, VerificationBy.class);
                        i.putExtra("verify_by", selected);
                        i.putExtra("email", s_emailAddress);
                        i.putExtra("fullname", fullName);
                        i.putExtra("phone", s_phoneNumber);
                        i.putExtra("username", username);
                        i.putExtra("response", response);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(VerificationOption.this, "Registration Failed 3, please try again", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                        System.out.println("Registration failed 3 = "+volleyError.getCause());
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("email", s_emailAddress);
                params.put("username", username);
                params.put("password", password);
                params.put("full_name", fullName);
                params.put("phone_number", s_phoneNumber);
                params.put("user_email_sign_up", String.valueOf(userEmailSignUp));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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

    @Override
    public void onBackPressed() {
        // do nothing
    }

//    private void VerifyPhone() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHONE_VERIFICATION,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("Signin info = "+response);
//                        try {
//                            myDialog.dismiss();
//                            JSONObject jsonResponse = new JSONObject(response);
//                            String message = jsonResponse.getString("message");
//
//                            // send to next activity
//                            Intent i = new Intent(VerificationOption.this, VerificationBy.class);
//                            i.putExtra("verify_by", selected);
//                            i.putExtra("email", s_emailAddress);
//                            i.putExtra("fullname", fullName);
//                            i.putExtra("phone", s_phoneNumber);
//                            i.putExtra("username", username);
//                            i.putExtra("message", message);
//                            startActivity(i);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            myDialog.dismiss();
//                            Toast.makeText(VerificationOption.this, "Verification Failed, please try again", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(VerificationOption.this, "Verification Failed, please try again", Toast.LENGTH_SHORT).show();
//                        myDialog.dismiss();
//                        if (volleyError.networkResponse != null){
//
//                            byte[] responseData = volleyError.networkResponse.data;
//                            if (responseData != null) {
//                                System.out.println("Phone verification error response = "+new String(responseData));
//                            }
//                        }
//                        volleyError.printStackTrace();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("phone_number", s_phoneNumber);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        requestQueue.add(stringRequest);
//        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//            @Override
//            public void onRequestFinished(Request<Object> request) {
//                requestQueue.getCache().clear();
//            }
//        });
//    }
//
//    private void VerifyEmail() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMAIL_VERIFICATION,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println("Signin info = "+response);
//                        try {
//                            myDialog.dismiss();
//                            JSONObject jsonResponse = new JSONObject(response);
//                            String message = jsonResponse.getString("message");
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            myDialog.dismiss();
//                            Toast.makeText(VerificationOption.this, "Email Verification Failed, please try again", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(VerificationOption.this, "Email Verification Failed, please try again", Toast.LENGTH_SHORT).show();
//                        myDialog.dismiss();
//                        if (volleyError.networkResponse != null){
//
//                            byte[] responseData = volleyError.networkResponse.data;
//                            if (responseData != null) {
//                                System.out.println("Email Verification error response = "+new String(responseData));
//                            }
//                        }
////                        System.out.println("Sign in error response = "+volleyError.getCause());
//                        volleyError.printStackTrace();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
//                params.put("email", s_emailAddress);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        requestQueue.add(stringRequest);
//        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//            @Override
//            public void onRequestFinished(Request<Object> request) {
//                requestQueue.getCache().clear();
//            }
//        });
//    }
}