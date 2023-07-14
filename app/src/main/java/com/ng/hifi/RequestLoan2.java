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
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestLoan2 extends AppCompatActivity {

    ImageView back;
    EditText fullName, posName, posAddress, amount;
    Boolean fullNameBool = false, posNameBool = false, posAddressBool = false, amountBool = false, imageUploadBool = false;
    LinearLayout uploadFile;
    LinearLayout text1;
    TextView text2;
    ImageView image;
    Button requestLoan;
    Dialog myDialogLoading;
    private RequestQueue rQueue;
    SharedPreferences preferences;
    String token;

    public static final String CREATE_LOAN = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/loans/create_get_user_loans/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_loan2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "not available");

        back = findViewById(R.id.back);
        //editview for entry
        fullName = findViewById(R.id.fullName);
        posName = findViewById(R.id.posName);
        posAddress = findViewById(R.id.posAddress);
        amount = findViewById(R.id.amount);

        //get upload button view
        uploadFile = findViewById(R.id.uploadFile);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        image = findViewById(R.id.image);
        requestLoan = findViewById(R.id.requestLoan);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestLoan2.this, HomeScreen.class);
                i.putExtra("from", "RequestLoan");
                startActivity(i);
            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (fullName.getText().length() >= 3){
                    fullName.setBackgroundResource(R.drawable.edit_text2);
                    fullNameBool = true;
                }
                checkerForButton("", Uri.parse(""));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        posName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (posName.getText().length() >= 5){
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
                if (posAddress.getText().length() >= 7){
                    posAddress.setBackgroundResource(R.drawable.edit_text2);
                    posAddressBool = true;
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
                    amountBool = true;
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

    public void checkerForButton(String displayName, Uri uri){
        if (fullNameBool && posNameBool && posAddressBool && amountBool && imageUploadBool){
            requestLoan.setBackgroundResource(R.drawable.button_black);
            requestLoan.setClickable(true);
            requestLoan.setTextColor(Color.WHITE);
            requestLoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send to DB
                    UploadInfo(displayName, uri);
                }
            });
        }
    }

    private void UploadInfo(final String fileName, Uri fileUri) {
        myDialogLoading = new Dialog(RequestLoan2.this);
        myDialogLoading.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialogLoading.findViewById(R.id.text);
        text.setText("Requesting for loan, please wait...");
        myDialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogLoading.setCanceledOnTouchOutside(false);
        myDialogLoading.show();

        InputStream iStream = null;
        try {

            iStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
            final byte[] inputData = getBytes(iStream);


            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, CREATE_LOAN,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            System.out.println("Create Loan response1 = "+ new String(response.data));
//                            Toast.makeText(getActivity(), "Upload Update "+response, Toast.LENGTH_SHORT).show();
//                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));

                                myDialogLoading.dismiss();
                                //create notification
                                createNotification();


                            } catch (JSONException e) {
                                myDialogLoading.dismiss();
                                e.printStackTrace();
                                Toast.makeText(RequestLoan2.this, "Error in loan application. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            myDialogLoading.dismiss();
                            Toast.makeText(RequestLoan2.this, "Network Error. Please try again", Toast.LENGTH_LONG).show();
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
                    params.put("requestor_full_name", fullName.getText().toString());
                    params.put("name_of_pos_oulet", posName.getText().toString());
                    params.put("address_of_pos_outlet", posAddress.getText().toString());
                    params.put("amount_requested_for", amount.getText().toString());
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("pos_image", new DataPart(fileName ,inputData));
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

        myDialogLoading = new Dialog(RequestLoan2.this);
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
//                            Toast.makeText(RequestLoan2.this, "Outlet Created successfully ", Toast.LENGTH_LONG).show();
                            // got to confirmation page
                            Intent i = new Intent(RequestLoan2.this, RequestLoanPending.class);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialogLoading.dismiss();
                            Toast.makeText(RequestLoan2.this, "Problem creating Notification", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RequestLoan2.this, new String(responseData), Toast.LENGTH_SHORT).show();
                            }
                        }
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("message", "You requested for loan of N"+amount.getText().toString());
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