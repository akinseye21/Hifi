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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class CreateAccount extends AppCompatActivity {

    EditText fullName, phoneNumber, emailAddress, password, username;
    RadioButton radioAcceptTerms;
    LinearLayout appleSignUp, googleSignUp, signIn;
    Button signUp;
    Boolean nameBool = false, phoneBool = false, emailBool = false, passwordBool = false, policyBool = false, usernameBool = false;
    int counter = 0;
    ImageView back;
    LinearLayout loading;

//    public static final String SIGN_UP = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/users/register/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        radioAcceptTerms = findViewById(R.id.radioAcceptTerms);
        appleSignUp = findViewById(R.id.appleSignUp);
        googleSignUp = findViewById(R.id.googleSignUp);
        loading = findViewById(R.id.loading);
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, SignIn.class));
            }
        });
        signUp = findViewById(R.id.signup);
        radioAcceptTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    policyBool = true;
                    checked();
                }else{
                    policyBool = false;
                    checked();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (fullName.getText().length()>6){
                    fullName.setBackgroundResource(R.drawable.edit_text2);
                    nameBool = true;
                }

                checked();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                counter = counter+1;
            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (phoneNumber.getText().length()>=11){
                    phoneNumber.setBackgroundResource(R.drawable.edit_text2);
                    phoneBool = true;
                }

                checked();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                counter = counter+1;
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
                counter = counter+1;
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
                counter = counter+1;
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (username.getText().length()>6){
                    username.setBackgroundResource(R.drawable.edit_text2);
                    usernameBool = true;
                }

                checked();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                counter = counter+1;
            }
        });

    }

    private void checked() {
        if (nameBool && phoneBool && emailBool && passwordBool && policyBool && usernameBool){
            signUp.setBackgroundResource(R.drawable.button_black);
            signUp.setClickable(true);
            signUp.setTextColor(Color.WHITE);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //go to the next screen for verification
                    Intent i = new Intent(CreateAccount.this, VerificationOption.class);
                    i.putExtra("fullname", fullName.getText().toString().trim());
                    i.putExtra("phone", phoneNumber.getText().toString().trim());
                    i.putExtra("email", emailAddress.getText().toString().trim());
                    i.putExtra("username", username.getText().toString().trim());
                    i.putExtra("password", password.getText().toString().trim());
                    startActivity(i);

                }
            });
        }
    }
}