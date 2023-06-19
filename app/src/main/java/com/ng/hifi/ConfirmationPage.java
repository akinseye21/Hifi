package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfirmationPage extends AppCompatActivity {

    ImageView back;
    Button home;
    TextView text1, text2;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        from = i.getStringExtra("from");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmationPage.this, HomeScreen.class);
                i.putExtra("from", "ConfirmationPage");
                startActivity(i);
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConfirmationPage.this, HomeScreen.class);
                i.putExtra("from", "ConfirmationPage");
                startActivity(i);
            }
        });

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        if (from.equals("BecFraudPrevention")){
            text1.setText("Verification Successful");
            text2.setText("Your payment has been successfully verified");
        }else if (from.equals("PayBackLoan")){
            text1.setText("Payment Successful");
            text2.setText("You have successfully paid back your loan");
        }


    }
}