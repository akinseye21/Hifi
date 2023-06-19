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

public class RequestLoan extends AppCompatActivity {

    ImageView back;
    EditText fullName, address, phoneNumber, bvn, nin, bankName, outletNumber, amount;
    Boolean fullNameBool = false, addressBool = false, phoneNumberBool = false, bvnBool = false, ninBool = false, bankNameBool = false, outletNumberBool = false, amountBool = false, imageUploadBool = false;
    LinearLayout uploadFile;
    LinearLayout text1;
    TextView text2;
    ImageView image;
    Button continued;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_loan);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        //editview for entry
        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);
        bvn = findViewById(R.id.bvn);
        nin = findViewById(R.id.nin);
        bankName = findViewById(R.id.bankName);
        outletNumber = findViewById(R.id.outletNumber);
        amount = findViewById(R.id.amount);
        //get upload button view
        uploadFile = findViewById(R.id.uploadFile);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        image = findViewById(R.id.image);
        continued = findViewById(R.id.continued);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestLoan.this, HomeScreen.class);
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
                checkerForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (address.getText().length() >= 3){
                    address.setBackgroundResource(R.drawable.edit_text2);
                    addressBool = true;
                }
                checkerForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (phoneNumber.getText().length() == 11){
                    phoneNumber.setBackgroundResource(R.drawable.edit_text2);
                    phoneNumberBool = true;
                }
                checkerForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bvn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (bvn.getText().length() >= 10){
                    bvn.setBackgroundResource(R.drawable.edit_text2);
                    bvnBool = true;
                }
                checkerForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nin.getText().length() >= 10){
                    nin.setBackgroundResource(R.drawable.edit_text2);
                    ninBool = true;
                }
                checkerForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (bankName.getText().length() >= 3){
                    bankName.setBackgroundResource(R.drawable.edit_text2);
                    bankNameBool = true;
                }
                checkerForButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        outletNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (outletNumber.getText().length() >= 3){
                    outletNumber.setBackgroundResource(R.drawable.edit_text2);
                    outletNumberBool = true;
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
                checkerForButton();
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

    public void checkerForButton(){
        if (fullNameBool && addressBool && phoneNumberBool && fullNameBool && bvnBool && ninBool && bankNameBool && outletNumberBool && amountBool && imageUploadBool){
            continued.setBackgroundResource(R.drawable.button_black);
            continued.setClickable(true);
            continued.setTextColor(Color.WHITE);
            continued.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(RequestLoan.this, RequestLoanPending.class);
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