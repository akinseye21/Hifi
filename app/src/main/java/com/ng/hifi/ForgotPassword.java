package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button continued;
    Boolean emailBool = false;
    Dialog myDialogLoading;

    public static final String FORGOT_PASSWORD = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/forgot_password/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email = findViewById(R.id.email);
        continued = findViewById(R.id.continued);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                    email.setBackgroundResource(R.drawable.edit_text2);
                    emailBool = true;
                }
                checkButton();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void checkButton() {

        if (emailBool){
            continued.setBackgroundResource(R.drawable.button_black);
            continued.setClickable(true);
            continued.setTextColor(Color.WHITE);
            continued.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // query DB

                    authUser();

                }
            });
        }
    }

    private void authUser() {

        myDialogLoading = new Dialog(ForgotPassword.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialogLoading.findViewById(R.id.text);
        text.setText("Authenticating Email...");
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Signin info = "+response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            myDialogLoading.dismiss();
                            Intent i = new Intent(ForgotPassword.this, PasswordReset1.class);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialogLoading.dismiss();
                            Toast.makeText(ForgotPassword.this, "Authentication Failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        myDialogLoading.dismiss();
                        System.out.println("Authentication error response");
                        Toast.makeText(ForgotPassword.this, "Authentication Error", Toast.LENGTH_SHORT).show();

                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("email", email.getText().toString().trim());
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