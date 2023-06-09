package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VerifyPhone extends AppCompatActivity {

    ImageView back;
    LinearLayout enterInput, successful;
    EditText edt1, edt2, edt3, edt4;
    TextView txtPhone;
    Button verify;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phone");

        enterInput = findViewById(R.id.enterInput);
        successful = findViewById(R.id.successfull);
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        txtPhone = findViewById(R.id.txtPhone);
        verify = findViewById(R.id.verify);
        back = findViewById(R.id.back);
        txtPhone.setText(phoneNumber);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterInput.setVisibility(View.GONE);
                successful.setVisibility(View.VISIBLE);
                verify.setText("Go to Home");
                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(VerifyPhone.this, SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}