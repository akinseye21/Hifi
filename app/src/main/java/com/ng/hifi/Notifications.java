package com.ng.hifi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notifications extends AppCompatActivity {

    ImageView back;
    TextView markAsRead;
    ListView listview;
    LinearLayout noNotification;
    SharedPreferences preferences;
    String token;
    Dialog myDialog;

    ArrayList<String> arr_message = new ArrayList<>();
    ArrayList<Boolean> arr_isRead = new ArrayList<>();
    ArrayList<String> arr_date = new ArrayList<>();

    public static final String GET_USER_NOTIFICATION = "https://gama-pay-26a021df6b2e.herokuapp.com/api/v1/notifications/create_get_user_notification/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        token = preferences.getString("token", "not available");

        back = findViewById(R.id.back);
        markAsRead = findViewById(R.id.markAsRead);
        listview = findViewById(R.id.listview);
        noNotification = findViewById(R.id.noNotification);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //get notifications
        getNotifications(token);

    }

    private void getNotifications(String token) {

        //show loading dialog
        myDialog = new Dialog(Notifications.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading your notification(s). Please wait...");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_USER_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int len = jsonArray.length();
                            if(len<1){
                                //show that there is no loan
                                noNotification.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.GONE);
                            }else{
                                //populate the loan in the listview
                                System.out.println("User notifications = "+jsonArray);

                                for (int i = len - 1; i >= 0; i--) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String message = jsonObject.getString("message");
                                    Boolean is_read = jsonObject.getBoolean("is_read");
                                    String date = jsonObject.getString("date_sent");

                                    arr_message.add(message);
                                    arr_isRead.add(is_read);
                                    arr_date.add(date);
                                }
                            }

                            NotificationAdapter notificationAdapter = new NotificationAdapter(Notifications.this, arr_message, arr_isRead, arr_date);
                            listview.setAdapter(notificationAdapter);

                            myDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(Notifications.this, "Could not get user notification(s)", Toast.LENGTH_SHORT).show();
                            System.out.println("Could not get user notifications");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        System.out.println("Notification error response = "+new String(volleyError.networkResponse.data));
                        Toast.makeText(Notifications.this, new String(volleyError.networkResponse.data), Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+token);//add authentication token
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Notifications.this);
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
}