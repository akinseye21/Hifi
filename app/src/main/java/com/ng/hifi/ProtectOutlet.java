package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProtectOutlet extends AppCompatActivity {

    Button protectOutlet;
    Dialog myDialog;

    //boolean for payment with card
    Boolean cardNameBool = false, cardNumberBool = false, cardExpDateBool = false, cardCvvBool = false;
    //boolean for payment with transfer
    Boolean bankNameBool = false, accountNumberBool = false, amountBool = false;
    String selection = "card";
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_outlet);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        protectOutlet = findViewById(R.id.protectOutlet);
        protectOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(ProtectOutlet.this);
                myDialog.setContentView(R.layout.custom_popup_notification);
                // Setting dialogview
                Window window = myDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                ImageView close = myDialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                TextView txtCard = myDialog.findViewById(R.id.txtCard);
                TextView txtTransfer = myDialog.findViewById(R.id.txtTransfer);
                LinearLayout linCard = myDialog.findViewById(R.id.linCard);
                LinearLayout linTransfer = myDialog.findViewById(R.id.linTransfer);
                pay = myDialog.findViewById(R.id.pay);

                txtCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtCard.setBackgroundResource(R.drawable.corner_white);
                        txtCard.setTextColor(Color.parseColor("#191616"));
                        txtTransfer.setBackground(null);
                        txtTransfer.setTextColor(Color.parseColor("#A1A1AA"));
                        linCard.setVisibility(View.VISIBLE);
                        linTransfer.setVisibility(View.GONE);
                        selection = "card";
                    }
                });
                txtTransfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtTransfer.setBackgroundResource(R.drawable.corner_white);
                        txtTransfer.setTextColor(Color.parseColor("#191616"));
                        txtCard.setBackground(null);
                        txtCard.setTextColor(Color.parseColor("#A1A1AA"));
                        linTransfer.setVisibility(View.VISIBLE);
                        linCard.setVisibility(View.GONE);
                        selection = "transfer";
                    }
                });

                //get EditTextView for card
                EditText cardName = myDialog.findViewById(R.id.cardName);
                EditText cardNumber = myDialog.findViewById(R.id.cardNumber);
                EditText expDate = myDialog.findViewById(R.id.expDate);
                EditText cvv = myDialog.findViewById(R.id.cvv);
                cardName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (cardName.getText().toString().length()>6){
                            cardName.setBackgroundResource(R.drawable.edit_text2);
                            cardNameBool = true;
                        }
                        checked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                cardNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (cardNumber.getText().toString().length() == 16){
                            cardNumber.setBackgroundResource(R.drawable.edit_text2);
                            cardNumberBool = true;
                        }

                        checked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                expDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (expDate.getText().toString().length() == 2){
                            StringBuilder stringBuilder = new StringBuilder(expDate.getText().toString());
                            stringBuilder.append('/');
                            String newString = stringBuilder.toString();
                            expDate.setText(newString);
                            int position = 3;
                            expDate.setSelection(position);
                        }

                        if(expDate.getText().toString().length() == 5){
                            expDate.setBackgroundResource(R.drawable.edit_text2);
                            cardExpDateBool = true;
                        }

                        checked();

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                cvv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (cvv.getText().toString().length() == 3){
                            cvv.setBackgroundResource(R.drawable.edit_text2);
                            cardCvvBool = true;
                        }

                        checked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                //get EditTextView for transfer
                EditText bankName = myDialog.findViewById(R.id.bank);
                EditText accNumber = myDialog.findViewById(R.id.accNumber);
                EditText amount = myDialog.findViewById(R.id.amount);
                bankName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (bankName.getText().toString().length()>6){
                            bankName.setBackgroundResource(R.drawable.edit_text2);
                            bankNameBool = true;
                        }
                        checked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                accNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (accNumber.getText().toString().length()==10){
                            accNumber.setBackgroundResource(R.drawable.edit_text2);
                            accountNumberBool = true;
                        }
                        checked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (amount.getText().toString().length()>2){
                            amount.setBackgroundResource(R.drawable.edit_text2);
                            amountBool = true;
                        }
                        checked();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


//                pay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (selection.equals("card")){
//                            //check booleans for card
//                            if (cardNameBool==true && cardNumberBool==true && cardExpDateBool==true && cardCvvBool==true){
//
//                            }
//                        }
//
//                        if (selection.equals("transfer")){
//                            //check booleans for transfer
//
//                        }
//                    }
//                });



                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
    }

    private void checked() {

        if (selection.equals("card")){
            //check booleans for card
            if (cardNameBool==true && cardNumberBool==true && cardExpDateBool==true && cardCvvBool==true){
                pay.setBackgroundResource(R.drawable.button_black);
                pay.setClickable(true);
                pay.setTextColor(Color.WHITE);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ProtectOutlet.this, ConfirmationPage.class));
                    }
                });
            }
        }

        if (selection.equals("transfer")){
            //check booleans for card
            if (bankNameBool==true && accountNumberBool==true && amountBool==true){
                pay.setBackgroundResource(R.drawable.button_black);
                pay.setClickable(true);
                pay.setTextColor(Color.WHITE);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ProtectOutlet.this, ConfirmationPage.class));
                    }
                });
            }
        }
    }
}