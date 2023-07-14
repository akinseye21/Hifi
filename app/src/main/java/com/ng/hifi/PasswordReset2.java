package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordReset2 extends AppCompatActivity {

    ImageView back;
    EditText password, confirmPassword;
    Button reset;
    EditText edt1, edt2, edt3, edt4;
    Dialog myDialog;

    String str_password;
    Boolean passwordBool = false, confirmPasswordBool = false;
    Boolean edt1Bool = false, edt2Bool = false, edt3Bool = false, edt4Bool = false;

    public static final String COMPLETE_FORGOT_PASSWORD = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/continue_forgot_password/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        reset = findViewById(R.id.reset);
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);

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
                checkButton();
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
                checkButton();
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
                checkButton();
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
                checkButton();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (password.getText().length() >= 8){
                    password.setBackgroundResource(R.drawable.edit_text2);
                    passwordBool = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_password = password.getText().toString();
                checkButton();
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (confirmPassword.getText().toString().equals(str_password)){
                    confirmPassword.setBackgroundResource(R.drawable.edit_text2);
                    confirmPasswordBool = true;
                }else{
                    int drawableResourceId = R.drawable.wrong;
                    Drawable drawable = ContextCompat.getDrawable(PasswordReset2.this, drawableResourceId);
                    confirmPassword.setError("Password do not match", drawable);
                }
                checkButton();
            }
        });
    }

    private void checkButton() {
        if (passwordBool && confirmPasswordBool && edt1Bool && edt2Bool && edt3Bool && edt4Bool){
            reset.setBackgroundResource(R.drawable.button_black);
            reset.setClickable(true);
            reset.setTextColor(Color.WHITE);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String myCode = edt1.getText().toString()+edt2.getText().toString()+edt3.getText().toString()+edt4.getText().toString();
                    // query DB
                    resetPassword(myCode);

                }
            });
        }
    }

    private void resetPassword(String myCode) {

        myDialog = new Dialog(PasswordReset2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Resetting password...\nPlease wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,COMPLETE_FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        myDialog.dismiss();
                        Toast.makeText(PasswordReset2.this, "Password change successful!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PasswordReset2.this, SignIn.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(PasswordReset2.this, "Password change failed, please try again", Toast.LENGTH_SHORT).show();
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
                params.put("password", password.getText().toString());
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