package com.ng.hifi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class BecFraudPrevention extends AppCompatActivity {

    ImageView back;
    EditText companyName, webUrl, companyLocation, fullName, amount;
    LinearLayout uploadFile;
    LinearLayout text1;
    TextView text2;
    ImageView image;
    Button verifyPayment;
    //boolean for entry information
    Boolean companyNameBool = false, webUrlBool = false, companyLocationBool = false, fullNameBool = false, amountBool = false, imageUploadBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bec_fraud_prevention);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
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
                checkerForButton();
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
                checkerForButton();
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
                checkerForButton();
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
                checkerForButton();
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
                intent.setType("application/pdf image/*");
                startActivityForResult(intent,1);
            }
        });


    }

    public void checkerForButton(){
        if (companyNameBool && webUrlBool && companyLocationBool && fullNameBool && amountBool && imageUploadBool){
            verifyPayment.setBackgroundResource(R.drawable.button_black);
            verifyPayment.setClickable(true);
            verifyPayment.setTextColor(Color.WHITE);
            verifyPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(BecFraudPrevention.this, ConfirmationPage.class);
                    i.putExtra("from", "BecFraudPrevention");
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
                        checkerForButton();
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
                checkerForButton();
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