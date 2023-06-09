package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class VerificationOption extends AppCompatActivity {

    ImageView back;
    RelativeLayout emailAddress, phoneNumber;
    RadioButton email, phone;
    Button createAccount;
    String selected = "";
    String fullName, s_phoneNumber, s_emailAddress, username;

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


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        emailAddress = findViewById(R.id.emailAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.radioEmail);
        phone = findViewById(R.id.radioPhone);
        createAccount = findViewById(R.id.continued);

        emailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress.setBackgroundResource(R.drawable.card2);
                email.setChecked(true);
                phoneNumber.setBackgroundResource(R.drawable.card1);
                phone.setChecked(false);
                selected = "email";
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
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerificationOption.this, VerificationBy.class);
                i.putExtra("verify_by", selected);
                i.putExtra("email", s_emailAddress);
                i.putExtra("fullname", fullName);
                i.putExtra("phone", s_phoneNumber);
                i.putExtra("username", username);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}