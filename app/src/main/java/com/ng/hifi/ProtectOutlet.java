package com.ng.hifi;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class ProtectOutlet extends AppCompatActivity {

    Button protectOutlet;
    Dialog myDialog, myDialogLoading;
    private RequestQueue rQueue;

    EditText posName, posAddress, posCity, posLandmark;
    LinearLayout upload;
    LinearLayout text1;
    TextView text2;
    ImageView image;
    SharedPreferences preferences;
    String token;

    //boolean for POS entry information
    Boolean posNameBool = false, posAddressBool = false, posCityBool = false, posLandmarkBool = false, imageUploadBool = false;
    //boolean for payment with card
    Boolean cardNameBool = false, cardNumberBool = false, cardExpDateBool = false, cardCvvBool = false;
    //boolean for payment with transfer
    Boolean bankNameBool = false, accountNumberBool = false, amountBool = false;
    String selection = "card";
    Button pay;

    // editText for the card details
    EditText cardName;
    EditText cardNumber;
    EditText expDate;
    EditText cvv;

    // string for number of outlets
    String numOutlets;
    TextView textHead;

    public static final String CREATE_POS_OUTLET = "https://gamaplaybackend-production.up.railway.app/api/v1/outlets/create/";
    public static final String CREATE_NOTIFICATION = "https://gamaplaybackend-production.up.railway.app/api/v1/notifications/create_get_user_notification/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_outlet);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        numOutlets = i.getStringExtra("outlet");

        PaystackSdk.initialize(getApplicationContext());
        setPaystackKey("pk_test_1f321cbb5ab36541925e805fd73e321f0ca07967");

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        posName = findViewById(R.id.name);
        posAddress = findViewById(R.id.address);
        posCity = findViewById(R.id.city);
        posLandmark = findViewById(R.id.landmark);
        upload = findViewById(R.id.upload);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        image = findViewById(R.id.image);
        textHead = findViewById(R.id.textHead);
        if (numOutlets.equals("multiple outlet")){
            textHead.setText("Buy cover for multiple outlet");
        }
        protectOutlet = findViewById(R.id.protectOutlet);

        posName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (posName.getText().length()>3){
                    posName.setBackgroundResource(R.drawable.edit_text2);
                    posNameBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        posAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (posAddress.getText().length()>3){
                    posAddress.setBackgroundResource(R.drawable.edit_text2);
                    posAddressBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        posCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (posCity.getText().length()>3){
                    posCity.setBackgroundResource(R.drawable.edit_text2);
                    posCityBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        posLandmark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (posLandmark.getText().length()>3){
                    posLandmark.setBackgroundResource(R.drawable.edit_text2);
                    posLandmarkBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }

        });

    }

    private void checkerForButton(String displayName, Uri uri) {
        if (posNameBool && posAddressBool && posCityBool && posLandmarkBool && imageUploadBool){
            protectOutlet.setBackgroundResource(R.drawable.button_black);
            protectOutlet.setClickable(true);
            protectOutlet.setTextColor(Color.WHITE);
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
            });

        }
    }

    public static void setPaystackKey(String publicKey) {
        PaystackSdk.setPublicKey(publicKey);
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
                            Toast.makeText(ProtectOutlet.this, "Invalid Card", Toast.LENGTH_SHORT).show();
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

    private void uploadInfo(final String fileName, Uri fileUri) {

        myDialogLoading = new Dialog(ProtectOutlet.this);
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
                                Toast.makeText(ProtectOutlet.this, "Error in creating outlet.", Toast.LENGTH_LONG).show();
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
                    params.put("name", posName.getText().toString());
                    params.put("cityState", posCity.getText().toString());
                    params.put("closeLandMark", posLandmark.getText().toString());
                    params.put("address", posAddress.getText().toString());
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

    private void createNotification() {

        myDialogLoading = new Dialog(ProtectOutlet.this);
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
                            Toast.makeText(ProtectOutlet.this, "Outlet Created successfully ", Toast.LENGTH_LONG).show();
                            // got to confirmation page
                            Intent i = new Intent(ProtectOutlet.this, ConfirmationPage.class);
                            i.putExtra("from", "ProtectOutlet");
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialogLoading.dismiss();
                            Toast.makeText(ProtectOutlet.this, "Problem creating Notification", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ProtectOutlet.this, new String(responseData), Toast.LENGTH_SHORT).show();
                            }
                        }
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("message", "You have successfully created POS outlet "+posName.getText().toString());
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
                        imageUploadBool = true;
                        checkerForButton(displayName, uri);
//                        file_path.setText(displayName);
//                        uploadPDF(displayName,uri);
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
                imageUploadBool = true;
                checkerForButton(displayName, uri);
//                file_path.setText(displayName);
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

    private void performCharge(String displayName, Uri uri, String theCardNumber, int expiryMonth, int expiryYear, String theCardCVV) {

        //checking card
        myDialogLoading = new Dialog(ProtectOutlet.this);
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
                Toast.makeText(ProtectOutlet.this, "Problem with card. "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Paystack Transaction error = "+error.getLocalizedMessage());
            }

        });
    }

}