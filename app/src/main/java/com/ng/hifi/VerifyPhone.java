package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class VerifyPhone extends AppCompatActivity {

    ImageView back;
    LinearLayout enterInput, successful;
    EditText edt1, edt2, edt3, edt4;
    TextView txtPhone, txtResponse;
    Button verify;
    String phoneNumber, response;
    Boolean edt1Bool = false, edt2Bool = false, edt3Bool = false, edt4Bool = false;
    Dialog myDialog;

    public static final String PHONE_VERIFICATION = "http://gamaplaybackend-production.up.railway.app/api/v1/users/verify_user_account_otp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phone");
        response = i.getStringExtra("response");

        enterInput = findViewById(R.id.enterInput);
        successful = findViewById(R.id.successfull);
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        txtPhone = findViewById(R.id.txtPhone);
        verify = findViewById(R.id.verify);
        back = findViewById(R.id.back);
        txtResponse = findViewById(R.id.txtResponse);
        txtResponse.setText(response);
        txtPhone.setText(phoneNumber);

        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {  // Change the condition as per your requirement
                    edt2.requestFocus();
                    edt1Bool = true;
                }
                checkForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {  // Change the condition as per your requirement
                    edt3.requestFocus();
                    edt2Bool = true;
                }
                checkForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {  // Change the condition as per your requirement
                    edt4.requestFocus();
                    edt3Bool = true;
                }
                checkForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {  // Change the condition as per your requirement
                    edt4Bool = true;
                }
                checkForButton();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void checkForButton() {
        if (edt1Bool && edt2Bool && edt3Bool && edt4Bool){
            verify.setBackgroundResource(R.drawable.button_black);
            verify.setClickable(true);
            verify.setTextColor(Color.WHITE);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String myCode = edt1.getText().toString()+edt2.getText().toString()+edt3.getText().toString()+edt4.getText().toString();

                    //send to DB for verification
                    VerifyAccount(myCode);


                }
            });
        }
    }

    private void VerifyAccount(String myCode) {

        myDialog = new Dialog(VerifyPhone.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Authenticating...\nPlease wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,PHONE_VERIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        myDialog.dismiss();
                        Toast.makeText(VerifyPhone.this, "Authentication successful, Kindly login.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyPhone.this, SignIn.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(VerifyPhone.this, "Authentication Failed 3, please try again", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                        System.out.println("Registration failed 3 = "+volleyError.getCause());
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("code", myCode);
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