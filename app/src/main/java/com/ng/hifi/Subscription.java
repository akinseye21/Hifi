package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class Subscription extends AppCompatActivity {

    Button proceed, pay;
    Dialog myDialog, myDialogLoading;
    String selection = "card";
    String plan = "";
    private RequestQueue rQueue;

    RelativeLayout month1, month6, month3, year1;
    RadioButton radioMonth1, radioMonth6, radioMonth3, radioYear1;

    //boolean for payment with card
    Boolean cardNameBool = false, cardNumberBool = false, cardExpDateBool = false, cardCvvBool = false;
    //boolean for payment with transfer
    Boolean bankNameBool = false, accountNumberBool = false, amountBool = false;
    // editText for the card details
    EditText cardName;
    EditText cardNumber;
    EditText expDate;
    EditText cvv;

    String displayName, myUri, name, cityState, closeLandMark, address;
    Uri uri;
    SharedPreferences preferences;
    String token;

    public static final String CREATE_POS_OUTLET = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/outlets/create/";
    public static final String CREATE_NOTIFICATION = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/notifications/create_get_user_notification/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        displayName = i.getStringExtra("displayName");
        myUri = i.getStringExtra("uri");
        uri = Uri.parse(myUri);
        name = i.getStringExtra("name");
        cityState = i.getStringExtra("cityState");
        closeLandMark = i.getStringExtra("closeLandMark");
        address = i.getStringExtra("address");

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        PaystackSdk.initialize(getApplicationContext());
        setPaystackKey("pk_test_1f321cbb5ab36541925e805fd73e321f0ca07967");

        month1 = findViewById(R.id.month1);
        month6 = findViewById(R.id.month6);
        month3 = findViewById(R.id.month3);
        year1 = findViewById(R.id.year1);
        radioMonth1 = findViewById(R.id.radioMonth1);
        radioMonth6 = findViewById(R.id.radioMonth6);
        radioMonth3 = findViewById(R.id.radioMonth3);
        radioYear1 = findViewById(R.id.radioYear1);

        month1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month1.setBackgroundResource(R.drawable.card2);
                month6.setBackgroundResource(R.drawable.card1);
                month3.setBackgroundResource(R.drawable.card1);
                year1.setBackgroundResource(R.drawable.card1);
                radioMonth1.setChecked(true);
                radioMonth6.setChecked(false);
                radioMonth3.setChecked(false);
                radioYear1.setChecked(false);
                plan = "one month";
            }
        });
        month6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month1.setBackgroundResource(R.drawable.card1);
                month6.setBackgroundResource(R.drawable.card2);
                month3.setBackgroundResource(R.drawable.card1);
                year1.setBackgroundResource(R.drawable.card1);
                radioMonth1.setChecked(false);
                radioMonth6.setChecked(true);
                radioMonth3.setChecked(false);
                radioYear1.setChecked(false);
                plan = "six month";
            }
        });
        month3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month1.setBackgroundResource(R.drawable.card1);
                month6.setBackgroundResource(R.drawable.card1);
                month3.setBackgroundResource(R.drawable.card2);
                year1.setBackgroundResource(R.drawable.card1);
                radioMonth1.setChecked(false);
                radioMonth6.setChecked(false);
                radioMonth3.setChecked(true);
                radioYear1.setChecked(false);
                plan = "three month";
            }
        });
        year1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month1.setBackgroundResource(R.drawable.card1);
                month6.setBackgroundResource(R.drawable.card1);
                month3.setBackgroundResource(R.drawable.card1);
                year1.setBackgroundResource(R.drawable.card2);
                radioMonth1.setChecked(false);
                radioMonth6.setChecked(false);
                radioMonth3.setChecked(false);
                radioYear1.setChecked(true);
                plan = "one year";
            }
        });


        proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (plan.equals("one month")){
                    dialoger(3500);
                }else if (plan.equals("six month")){
                    dialoger(18000);
                }else if (plan.equals("three month")){
                    dialoger(9000);
                }else if (plan.equals("one year")){
                    dialoger(36000);
                }else{
                    Toast.makeText(Subscription.this, "Please select a subscription plan", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void dialoger(int price) {
        myDialog = new Dialog(Subscription.this);
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
                checked(price, displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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

                checked(price, displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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

                checked(price, displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());

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

                checked(price, displayName, uri, cardNumber.getText().toString(), expDate.getText().toString(), cvv.getText().toString());
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

    private void checked(int price, String displayName, Uri uri, String theCardNumber, String theCardExpiration, String theCardCVV) {

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
                            performCharge(price, displayName, uri, theCardNumber, expiryMonth, expiryYear, theCardCVV);
                        } else {
                            //do something
                            Toast.makeText(Subscription.this, "Invalid Card", Toast.LENGTH_SHORT).show();
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

    private void performCharge(int price, String displayName, Uri uri, String theCardNumber, int expiryMonth, int expiryYear, String theCardCVV) {

        //checking card
        myDialogLoading = new Dialog(Subscription.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialogLoading.findViewById(R.id.text);
        text.setText("Checking card...");
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();


        Card card = new Card(theCardNumber, expiryMonth, expiryYear, theCardCVV);

        Charge charge = new Charge();
        charge.setAmount(price*100);
        charge.setEmail("abayomi.akinseye@gmail.com");
        charge.setCard(card);

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                System.out.println("Paystack Transaction = "+transaction);
//                startActivity(new Intent(ProtectOutlet.this, ConfirmationPage.class));
//                parseResponse(transaction.getReference());
                myDialogLoading.dismiss();
                uploadInfo(displayName, uri);
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
                Toast.makeText(Subscription.this, "Problem with card. "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Paystack Transaction error = "+error.getLocalizedMessage());
            }

        });
    }

    private void uploadInfo(final String fileName, Uri fileUri) {

        myDialogLoading = new Dialog(Subscription.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();

        InputStream iStream = null;
        try {

            iStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
            final byte[] inputData = getBytes(iStream);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, CREATE_POS_OUTLET,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            System.out.println("Create POS Outlet response1 = "+ new String(response.data));
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
                                Toast.makeText(Subscription.this, "Error in creating outlet.", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialogLoading.dismiss();
                            System.out.println("Create POS Outlet error2 = "+ error.getMessage());
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
                    params.put("name", name);
                    params.put("cityState", cityState);
                    params.put("closeLandMark", closeLandMark);
                    params.put("address", address);
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("frontPicture", new DataPart(fileName ,inputData));
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

    private void createNotification() {

        myDialogLoading = new Dialog(Subscription.this);
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
                            String id = jsonResponse.getString("id");
                            String message = jsonResponse.getString("message");
                            String sentTo = jsonResponse.getString("sentTo");
                            String is_read = jsonResponse.getString("is_read");
                            String date = jsonResponse.getString("date_sent");

                            myDialogLoading.dismiss();
                            Toast.makeText(Subscription.this, "Outlet Created successfully ", Toast.LENGTH_LONG).show();
                            // got to confirmation page
                            Intent i = new Intent(Subscription.this, ConfirmationPage.class);
                            i.putExtra("from", "ProtectOutlet");
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialogLoading.dismiss();
                            Toast.makeText(Subscription.this, "Problem creating Notification", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Subscription.this, new String(responseData), Toast.LENGTH_SHORT).show();
                            }
                        }
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("message", "You have successfully created POS outlet "+name);
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
}