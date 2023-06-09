package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    EditText emailAddress, password;
    Boolean emailBool = false, passwordBool = false;
    Button signIn;
    LinearLayout signUp;
    LinearLayout loading;
    SharedPreferences preferences;

    public static final String SIGN_IN = "https://gamaplaybackend-production.up.railway.app/api/v1/users/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signUp);
        loading = findViewById(R.id.loading);

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
                                loading.setVisibility(View.GONE);
                                //save in sharedpreferences
                                preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor myEdit = preferences.edit();
                                myEdit.putString( "email", email);
                                myEdit.putString( "username", username);
                                myEdit.putString("fullname", full_name);
                                myEdit.putString("phonenumber", phone_number);
                                myEdit.commit();
                                //send aas intent
                                Intent i = new Intent(SignIn.this, HomeScreen.class);
                                i.putExtra("email", email);
                                i.putExtra("username", username);
                                i.putExtra("fullname", full_name);
                                i.putExtra("phonenumber", phone_number);
                                startActivity(i);
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
                        Toast.makeText(SignIn.this, "Authentication Failed, please try again", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        System.out.println("Sign in error response = "+volleyError.getCause());
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
}