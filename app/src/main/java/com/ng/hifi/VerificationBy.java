package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationBy extends AppCompatActivity {

    ImageView back, image;
    TextView txt1, txt2, txt4, changeEmail, txtEmail;
    LinearLayout lin_txt3;
    Button openEmail;
    String verify_by;
    String fullName, s_phoneNumber, s_emailAddress, username, response;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_by);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        image = findViewById(R.id.image);
        txt1 = findViewById(R.id.text1);
        txt2 = findViewById(R.id.text2);
        lin_txt3 = findViewById(R.id.lin_text3);
        txt4 = findViewById(R.id.text4);
        openEmail = findViewById(R.id.buttonOpen);
        changeEmail = findViewById(R.id.changeEmail);
        txtEmail = findViewById(R.id.txt_email);

        Intent i = getIntent();
        verify_by = i.getStringExtra("verify_by");
        fullName = i.getStringExtra("fullname");
        s_phoneNumber = i.getStringExtra("phone");
        s_emailAddress = i.getStringExtra("email");
        username = i.getStringExtra("username");
        response = i.getStringExtra("response");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (verify_by.equals("phone")){
            image.setImageResource(R.drawable.img6);
            txt1.setText("Phone Verification");
//            txt2.setText("...............");
            txt2.setText("You have received a 4-digit code to verify");
            lin_txt3.setVisibility(View.GONE);
            txt4.setVisibility(View.GONE);
//            txt4.setText("\n"+response);
            openEmail.setText("Continue");
            openEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(VerificationBy.this, VerifyPhone.class);
                    i.putExtra("response", response);
                    i.putExtra("phone", s_phoneNumber);
                    startActivity(i);
                }
            });
            changeEmail.setVisibility(View.GONE);
        }else if (verify_by.equals("email")){
            txtEmail.setText(s_emailAddress);
            openEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Use the email API to send authentication link to user email


                    //intent to open email in user phone
                    Intent mailClient = new Intent(Intent.ACTION_VIEW);
                    mailClient.addCategory(Intent.CATEGORY_APP_EMAIL);
                    mailClient.setPackage("com.google.android.gm");
                    // Verify that there is an activity that can handle the intent
                    PackageManager packageManager = getPackageManager();
                    if (mailClient.resolveActivity(packageManager) != null) {
                        startActivity(mailClient);
                    } else {
                        // Handle the case when the Gmail app is not installed
                        Toast.makeText(VerificationBy.this, "No mail client installed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}