package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VerificationBy extends AppCompatActivity {

    ImageView back, image;
    TextView txt1, txt2, txt4, changeEmail, txtEmail;
    LinearLayout lin_txt3;
    Button openEmail;
    String verify_by;
    String fullName, s_phoneNumber, s_emailAddress, username;

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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (verify_by.equals("phone")){
            image.setImageResource(R.drawable.img6);
            txt1.setText("Phone Verification");
            txt2.setText("You will receive a 4-digit code to verify");
            lin_txt3.setVisibility(View.GONE);
            txt4.setVisibility(View.GONE);
            openEmail.setText("Continue");
            openEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(VerificationBy.this, VerifyPhone.class);
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
                    //intent to open email in user phone
                    Intent mailClient = new Intent(Intent.ACTION_VIEW);
                    mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
                    startActivity(mailClient);
                }
            });
        }
    }
}