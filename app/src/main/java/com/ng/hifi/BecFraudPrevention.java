package com.ng.hifi;

import static com.ng.hifi.ProtectOutlet.CREATE_NOTIFICATION;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class BecFraudPrevention extends AppCompatActivity {

    ImageView back;
    ScrollView scrollView;
    EditText companyName, webUrl, companyLocation, fullName, amount;
    LinearLayout view2, uploadFile;
    LinearLayout text1;
    TextView text2;
    TextView text_1, text_2;
    ImageView image;
    Button verifyPayment, pay;
    Dialog myDialogLoading;
    Dialog myDialog;
    private RequestQueue rQueue;
    SharedPreferences preferences;
    String token;
    String selection = "card";

    // editText for the card details
    EditText cardName;
    EditText cardNumber;
    EditText expDate;
    EditText cvv;

    //boolean for payment with card
    Boolean cardNameBool = false, cardNumberBool = false, cardExpDateBool = false, cardCvvBool = false;
    //boolean for payment with transfer
    Boolean bankNameBool = false, accountNumberBool = false, amountBool = false;

    //boolean for entry information
    Boolean companyNameBool = false, webUrlBool = false, companyLocationBool = false, fullNameBool = false, amountBool1 = false, imageUploadBool1 = false;

    public static final String CREATE_BEC = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/loans/create_get_bec_fraud/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bec_fraud_prevention);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        PaystackSdk.initialize(getApplicationContext());
        setPaystackKey("pk_test_1f321cbb5ab36541925e805fd73e321f0ca07967");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        scrollView = findViewById(R.id.scroll);
        view2 = findViewById(R.id.view2);
        text_1 = findViewById(R.id.text_1);
        text_2 = findViewById(R.id.text_2);
        //get Edittext views
        companyName = findViewById(R.id.companyName);
        webUrl = findViewById(R.id.webUrl);
        companyLocation = findViewById(R.id.companyLocation);
        fullName = findViewById(R.id.requestorName);
        amount = findViewById(R.id.amount);
        verifyPayment = findViewById(R.id.verifyPayment);
        //get upload button view
        uploadFile = findViewById(R.id.uploadFile);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        image = findViewById(R.id.image);

        companyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (companyName.getText().length()>3){
                    companyName.setBackgroundResource(R.drawable.edit_text2);
                    companyNameBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        webUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Boolean checker = false;
                try {
                    new URL(webUrl.getText().toString());
                    checker = true;
                } catch (Exception e) {
                    checker = false;
                }

                if (checker){
                    webUrl.setBackgroundResource(R.drawable.edit_text2);
                    webUrlBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        companyLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (companyLocation.getText().length()>=5){
                    companyLocation.setBackgroundResource(R.drawable.edit_text2);
                    companyLocationBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (fullName.getText().length()>=5){
                    fullName.setBackgroundResource(R.drawable.edit_text2);
                    fullNameBool = true;
                }
                checkerForButton("", Uri.parse(""));
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
                if (amount.getText().length() >= 3){
                    amount.setBackgroundResource(R.drawable.edit_text2);
                    amountBool1 = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });


    }

    public void checkerForButton(final String fileName, Uri fileUri){
        if (companyNameBool && webUrlBool && companyLocationBool && fullNameBool && amountBool1 && imageUploadBool1){
            verifyPayment.setBackgroundResource(R.drawable.button_black);
            verifyPayment.setClickable(true);
            verifyPayment.setTextColor(Color.WHITE);
            verifyPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    scrollView.setVisibility(View.GONE);
                    text_1.setVisibility(View.GONE);
                    text_2.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    verifyPayment.setText("Proceed to payment");
                    verifyPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //show payment access
                            paymentAccess(fileName, fileUri);

                        }
                    });


                }
            });
        }
    }

    private void paymentAccess(String displayName, Uri uri) {
        myDialog = new Dialog(BecFraudPrevention.this);
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
                checked(displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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

                checked(displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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

                checked(displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());

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

                checked(displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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
                checked2(displayName, uri, bankName.getText().toString(), accNumber.getText().toString(), amount.getText().toString());
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
                checked2(displayName, uri, bankName.getText().toString(), accNumber.getText().toString(), amount.getText().toString());
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
                checked2(displayName, uri, bankName.getText().toString(), accNumber.getText().toString(), amount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    private void checked(String displayName, Uri uri, String theCardNumber, String theCardExpiration, String theCardCVV) {

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
                            performCharge(displayName, uri, theCardNumber, expiryMonth, expiryYear, theCardCVV);
                        } else {
                            //do something
                            Toast.makeText(BecFraudPrevention.this, "Invalid Card", Toast.LENGTH_SHORT).show();
                        }

                        // integrate paystack gateway  here

                        // if payment is successful, save in the DB-API

                    }
                });
            }
        }


    }

    private void checked2(String displayName, Uri uri, String theBankName, String theBankAccount, String theAmount){

        if (selection.equals("transfer")){
            //check booleans for card
            if (bankNameBool==true && accountNumberBool==true && amountBool==true){
                pay.setBackgroundResource(R.drawable.button_black);
                pay.setClickable(true);
                pay.setTextColor(Color.WHITE);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // integrate paystack gateway here

                        //get the account number
                        //verify the account number
                        //get bank codes from API https://api.paystack.co/bank?currency=NGN or https://api.paystack.co/bank?currency=NGN&type=nuban (GET) request
                        //create transfer recipient - make a POST request to the Transfer RecipientAPI passing customer’s bank account detail:
                        /*
                            curl https://api.paystack.co/transferrecipient
                            -H "Authorization: Bearer YOUR_SECRET_KEY"
                            -H "Content-Type: application/json"
                            -d '{ "type": "nuban",
                                    "name": "John Doe",
                                    "account_number": "0001234567",
                                    "bank_code": "058",
                                    "currency": "NGN"
                                }'
                            -X POST
                         */
                        //from the response, get the 'recipient_code'
                        //generate a transfer reference



                        // if payment is successful, save in the DB-API
//                        uploadInfo(displayName, uri);
                    }
                });
            }
        }

    }

    private void performCharge(String displayName, Uri uri, String theCardNumber, int expiryMonth, int expiryYear, String theCardCVV) {

        //checking card
        myDialogLoading = new Dialog(BecFraudPrevention.this);
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
//                startActivity(new Intent(ProtectOutlet.this, ConfirmationPage.class));
//                parseResponse(transaction.getReference());
                myDialogLoading.dismiss();

                //send to DB
                createBEC(displayName, uri);
//                uploadInfo(displayName, uri);
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
                Toast.makeText(BecFraudPrevention.this, "Problem with card. "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Paystack Transaction error = "+error.getLocalizedMessage());
            }

        });
    }

    private void createBEC(String fileName, Uri fileUri) {
        myDialogLoading = new Dialog(BecFraudPrevention.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialogLoading.findViewById(R.id.text);
        text.setText("Creating your request...");
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();

        InputStream iStream = null;
        try {

            iStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
            final byte[] inputData = getBytes(iStream);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, CREATE_BEC,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            System.out.println("BEC FraudPreventing response1 = "+ new String(response.data));
//                            Toast.makeText(getActivity(), "Upload Update "+response, Toast.LENGTH_SHORT).show();
//                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String message = jsonObject.getString("message");
                                String data = jsonObject.getString("data");

                                if(message.equals("Outlet created successfully")){
                                    myDialogLoading.dismiss();
                                    //create Notification here
                                    createNotification();

                                }

                            } catch (JSONException e) {
                                myDialogLoading.dismiss();
                                e.printStackTrace();
                                Toast.makeText(BecFraudPrevention.this, "Error in creating request1.", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialogLoading.dismiss();


                            if (error.networkResponse != null) {
                                // Retrieve the response body if available
                                byte[] responseData = error.networkResponse.data;
                                if (responseData != null) {
                                    String message = new String(responseData);

                                    Toast.makeText(BecFraudPrevention.this, "Error in creating request2.", Toast.LENGTH_LONG).show();
                                    System.out.println("Error in creating request "+ message);
                                }
                            }
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("company_name", companyName.getText().toString());
                    params.put("company_url", webUrl.getText().toString());
                    params.put("company_location", companyLocation.getText().toString());
                    params.put("name_of_person_asking_for_payment", fullName.getText().toString());
                    params.put("amount", amount.getText().toString());
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("invoice_file", new DataPart(fileName ,inputData));
                    return params;
                }

                /*
                 *pass authentication token
                 */
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Token "+token);//add authentication token
                    return headers;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(getApplicationContext());
            rQueue.add(volleyMultipartRequest);



        } catch (FileNotFoundException e) {
            myDialogLoading.dismiss();
            System.out.println("File Error = "+ e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            myDialogLoading.dismiss();
            System.out.println("IOException = "+ e.getMessage());
            e.printStackTrace();
        }


    }

    private void createNotification() {

        myDialogLoading = new Dialog(BecFraudPrevention.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialogLoading.findViewById(R.id.text);
        text.setText("Getting object data...");
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Notification response = "+response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            myDialogLoading.dismiss();
                            Toast.makeText(BecFraudPrevention.this, "BEC Created successfully ", Toast.LENGTH_LONG).show();
                            // got to confirmation page
                            Intent i = new Intent(BecFraudPrevention.this, ConfirmationPage.class);
                            i.putExtra("from", "BecFraudPrevention");
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialogLoading.dismiss();
                            Toast.makeText(BecFraudPrevention.this, "Problem creating BEC", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialogLoading.dismiss();
                        if (volleyError.networkResponse != null){

                            byte[] responseData = volleyError.networkResponse.data;
                            if (responseData != null) {
                                System.out.println("Error creating notification "+new String(responseData));
                                Toast.makeText(BecFraudPrevention.this, new String(responseData), Toast.LENGTH_SHORT).show();
                            }
                        }
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("message", "You have successfully requested a BEC with name "+companyName.getText().toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+token);//add authentication token
                return headers;
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

    public static void setPaystackKey(String publicKey) {
        PaystackSdk.setPublicKey(publicKey);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
//            String path = myFile.getAbsolutePath();
            String displayName = null;
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ",displayName);

                        text1.setVisibility(View.GONE);
                        text2.setText(displayName);
                        text2.setTextColor(Color.parseColor("#191616"));
                        image.setVisibility(View.VISIBLE);
                        image.setImageURI(uri);
                        imageUploadBool1 = true;
                        checkerForButton(displayName, uri);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ",displayName);

                text1.setVisibility(View.GONE);
                text2.setText(displayName);
                text2.setTextColor(Color.parseColor("#191616"));
                image.setVisibility(View.VISIBLE);
                image.setImageURI(uri);
                imageUploadBool1 = true;
                checkerForButton(displayName, uri);
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}