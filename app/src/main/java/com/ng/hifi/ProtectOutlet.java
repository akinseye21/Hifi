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
    ImageView back;
    EditText posName, posAddress, posCity, posLandmark;
    LinearLayout upload;
    LinearLayout text1;
    TextView text2;
    ImageView image;


    //boolean for POS entry information
    Boolean posNameBool = false, posAddressBool = false, posCityBool = false, posLandmarkBool = false, imageUploadBool = false;

    // string for number of outlets
    String numOutlets;
    TextView textHead;

    public static final String CREATE_NOTIFICATION = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/notifications/create_get_user_notification/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_outlet);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        numOutlets = i.getStringExtra("outlet");


        protectOutlet = findViewById(R.id.protectOutlet);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        posName = findViewById(R.id.name);
        posAddress = findViewById(R.id.address);
        posCity = findViewById(R.id.city);
        posLandmark = findViewById(R.id.landmark);
        upload = findViewById(R.id.upload);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        image = findViewById(R.id.image);




        if (numOutlets.equals("multiple outlet")){
            textHead.setText("Buy cover for multiple outlet");
        }

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

                    Intent i = new Intent(ProtectOutlet.this, Subscription.class);
                    i.putExtra("displayName", displayName);
                    i.putExtra("uri", uri.toString());
                    i.putExtra("name", posName.getText().toString());
                    i.putExtra("cityState", posCity.getText().toString());
                    i.putExtra("closeLandMark", posLandmark.getText().toString());
                    i.putExtra("address", posAddress.getText().toString());
                    startActivity(i);
                }
            });

        }
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




}