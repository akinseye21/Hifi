package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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

public class SignIn extends AppCompatActivity {

    ImageView back;
    EditText emailAddress, password;
    Boolean emailBool = false, passwordBool = false;
    Button signIn;
    LinearLayout signUp;
    LinearLayout loading;
    TextView forgotPass;
    SharedPreferences preferences;

    public static final String SIGN_IN = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/login/";
    public static final String GET_REF_CODE = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/get_user_referal_code/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, MainActivity.class));
            }
        });
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signUp);
        loading = findViewById(R.id.loading);
        forgotPass = findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, ForgotPassword.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, CreateAccount.class));
            }
        });
        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString().trim()).matches()){
                    emailAddress.setBackgroundResource(R.drawable.edit_text2);
                    emailBool = true;
                }

                checked();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailBool = true;
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (password.getText().length()>7){
                    password.setBackgroundResource(R.drawable.edit_text2);
                    passwordBool = true;
                }

                checked();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordBool = true;
            }
        });
    }

    private void checked() {
        if (emailBool && passwordBool){
            signIn.setBackgroundResource(R.drawable.button_black);
            signIn.setClickable(true);
            signIn.setTextColor(Color.WHITE);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // query DB
                    loading.setVisibility(View.VISIBLE);
                    SignInUser();

                }
            });
        }
    }

    private void SignInUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGN_IN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Signin info = "+response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String user = jsonResponse.getString("user");
                            String token = jsonResponse.getString("token");

                            JSONObject jsonResponse2 = new JSONObject(user);
                            String email = jsonResponse2.getString("email");
                            String username = jsonResponse2.getString("username");
                            String full_name = jsonResponse2.getString("full_name");
                            String phone_number = jsonResponse2.getString("phone_number");
                            String outlets = jsonResponse2.getString("outlets");

                            System.out.println("Email = "+email+"\nUsername = "+username+"\nFullname = "+full_name+"\nPhone = "+phone_number+
                                    "\nToken = "+token);

                            JSONArray jsonArray = new JSONArray(outlets);
                            int length = jsonArray.length();
                            for (int i=0; i<length; i++){
                                //extract the values of the objects in the array
                                //or extract the key:value pair in the array as the case may be
                            }

                            if (!token.equals("")){
                                //get user referral code
                                getRefCode(token, email, username, full_name, phone_number);
                            }else{
                                loading.setVisibility(View.GONE);
                                Toast.makeText(SignIn.this, "Authentication Failed, please try again", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(SignIn.this, "Authentication Failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        loading.setVisibility(View.GONE);
                        if (volleyError.networkResponse != null){

                            byte[] responseData = volleyError.networkResponse.data;
                            if (responseData != null) {
                                String new_response = new String(responseData);
                                try {
                                    JSONObject jsonObject = new JSONObject(new_response);
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SignIn.this, message, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                System.out.println("Sign in error response = "+new String(responseData));

                            }
                        }
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("email", emailAddress.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
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

    private void getRefCode(String token, String email, String username, String full_name, String phone_number) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_REF_CODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String code = jsonResponse.getString("code");
                            //add code to the sharedpreference

                            loading.setVisibility(View.GONE);
                            //save in sharedpreferences
                            preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor myEdit = preferences.edit();
                            myEdit.putString( "email", email);
                            myEdit.putString( "username", username);
                            myEdit.putString("fullname", full_name);
                            myEdit.putString("phonenumber", phone_number);
                            myEdit.putString("referralCode", code);
                            myEdit.putString("token", token);
                            myEdit.commit();
                            //send as intent
                            Intent i = new Intent(SignIn.this, HomeScreen.class);
                            i.putExtra("from", "SignIn");
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(SignIn.this, "Could not get user code", Toast.LENGTH_SHORT).show();
                            System.out.println("Could not get referral code for user");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(SignIn.this, "Could not get user code", Toast.LENGTH_SHORT).show();
                        System.out.println("Sign in error response = "+new String(volleyError.networkResponse.data));
                        Toast.makeText(SignIn.this, new String(volleyError.networkResponse.data), Toast.LENGTH_SHORT).show();
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
}