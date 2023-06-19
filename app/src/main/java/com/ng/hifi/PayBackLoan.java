package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PayBackLoan extends AppCompatActivity {

    Dialog myDialogLoading;
    ImageView back;
    RelativeLayout fullPayment, halfPayment;
    RadioButton full, half;
    Button payBack;
    String selected = "";
    String selection = "card";
    Button pay;

    // editText for the card details
    EditText cardName;
    EditText cardNumber;
    EditText expDate;
    EditText cvv;

    //boolean for payment with card
    Boolean cardNameBool = false, cardNumberBool = false, cardExpDateBool = false, cardCvvBool = false;
    //boolean for payment with transfer
    Boolean bankNameBool = false, accountNumberBool = false, amountBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_back_loan);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        PaystackSdk.initialize(getApplicationContext());
        setPaystackKey("pk_test_1f321cbb5ab36541925e805fd73e321f0ca07967");

        fullPayment = findViewById(R.id.fullPayment);
        halfPayment = findViewById(R.id.halfPayment);
        full = findViewById(R.id.radioFull);
        half = findViewById(R.id.radioHalf);
        payBack = findViewById(R.id.payBack);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fullPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullPayment.setBackgroundResource(R.drawable.card2);
                full.setChecked(true);
                halfPayment.setBackgroundResource(R.drawable.card1);
                half.setChecked(false);
                selected = "full";
                payBack.setBackgroundResource(R.drawable.button_black);
                payBack.setClickable(true);
                payBack.setTextColor(Color.WHITE);
            }
        });

        halfPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullPayment.setBackgroundResource(R.drawable.card1);
                full.setChecked(false);
                halfPayment.setBackgroundResource(R.drawable.card2);
                half.setChecked(true);
                selected = "half";
                payBack.setBackgroundResource(R.drawable.button_black);
                payBack.setClickable(true);
                payBack.setTextColor(Color.WHITE);
            }
        });

        payBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show the dialog popup notification
                Dialog myDialog = new Dialog(PayBackLoan.this);
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
                cardName = myDialog.findViewById(R.id.cardName);
                cardNumber = myDialog.findViewById(R.id.cardNumber);
                expDate = myDialog.findViewById(R.id.expDate);
                cvv = myDialog.findViewById(R.id.cvv);
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
                        checked(cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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
                        if (cardNumber.getText().toString().length() >= 16){
                            cardNumber.setBackgroundResource(R.drawable.edit_text2);
                            cardNumberBool = true;
                        }

                        checked(cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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

                        checked(cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());

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
                        checked(cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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
                        checked2(bankName.getText().toString(), accNumber.getText().toString(), amount.getText().toString());
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
                        checked2(bankName.getText().toString(), accNumber.getText().toString(), amount.getText().toString());
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
                        checked2(bankName.getText().toString(), accNumber.getText().toString(), amount.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });
    }

    private void checked(String theCardNumber, String theCardExpiration, String theCardCVV) {

        if (selection.equals("card")){
            //check booleans for card
            if (cardNameBool==true && cardNumberBool==true && cardExpDateBool==true && cardCvvBool==true){
                pay.setBackgroundResource(R.drawable.button_black);
                pay.setClickable(true);
                pay.setTextColor(Color.WHITE);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] cardExpiryArray = theCardExpiration.split("/");
                        int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
                        int expiryYear = Integer.parseInt(cardExpiryArray[1]);

                        Card card = new Card(theCardNumber, expiryMonth, expiryYear, theCardCVV);
                        if (card.isValid()) {
                            // charge card
                            performCharge(theCardNumber, expiryMonth, expiryYear, theCardCVV);
                        } else {
                            //do something
                            Toast.makeText(PayBackLoan.this, "Invalid Card", Toast.LENGTH_SHORT).show();
                        }

                        // integrate paystack gateway  here

                        // if payment is successful, save in the DB-API
//                        uploadInfo(displayName, uri);
                    }
                });
            }
        }


    }

    private void checked2(String theBankName, String theBankAccount, String theAmount){

        if (selection.equals("transfer")){
            //check booleans for card
            if (bankNameBool==true && accountNumberBool==true && amountBool==true){
                pay.setBackgroundResource(R.drawable.button_black);
                pay.setClickable(true);
                pay.setTextColor(Color.WHITE);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // integrate paystack gateway  here

                        //get bank codes from API https://api.paystack.co/bank
                        //


                        // if payment is successful, save in the DB-API
                    }
                });
            }
        }

    }

    private void performCharge(String theCardNumber, int expiryMonth, int expiryYear, String theCardCVV) {

        //checking card
        myDialogLoading = new Dialog(PayBackLoan.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialogLoading.findViewById(R.id.text);
        text.setText("Checking card...");
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();

        Card card = new Card(theCardNumber, expiryMonth, expiryYear, theCardCVV);

        Charge charge = new Charge();
        charge.setAmount(1000000*100);
        charge.setEmail("abayomi.akinseye@gmail.com");
        charge.setCard(card);

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                System.out.println("Paystack Transaction = "+transaction);


                myDialogLoading.dismiss();
                uploadInfo();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
                System.out.println("Paystack Before Validate = "+transaction.getReference());
//                Log.d("Main Activity", "beforeValidate: " + transaction.getReference());
            }


            @Override
            public void onError(Throwable error, Transaction transaction) {
                myDialogLoading.dismiss();
                Toast.makeText(PayBackLoan.this, "Problem with card. "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Paystack Transaction error = "+error.getLocalizedMessage());
            }

        });
    }

    public void uploadInfo() {

        //the pay back loan API will be used here

//                Intent i = new Intent(PayBackLoan.this, ConfirmationPage.class);
//                i.putExtra("from", "PayBackLoan");
//                startActivity(i);
    }

    public static void setPaystackKey(String publicKey) {
        PaystackSdk.setPublicKey(publicKey);
    }

}