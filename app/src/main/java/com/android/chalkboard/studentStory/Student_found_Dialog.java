package com.android.chalkboard.studentStory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Student_found_Dialog extends Dialog {

    private AlertDialog alertDialog;
    private Context context;
    private String name,mobile;
    private int id;
    Qr_Code_Reader qr_code_reader;
    private CustomDialog customDialog;
    private RequestQueue requestQueue;
    Typeface typeface;

    public Student_found_Dialog(@NonNull Context context,String name,String mobile,int id,Qr_Code_Reader qr_code_reader,CustomDialog customDialog) {
        super(context);
        this.context = context;
        this.name = name;
        this.mobile = mobile;
        this.id = id;
        this.qr_code_reader = qr_code_reader;
        this. customDialog = customDialog;
        typeface = Typeface.createFromAsset(context.getAssets(),"muli_black.ttf");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_found);

    }

    public void show(LayoutInflater inflater) {
        alertDialog = new AlertDialog.Builder(context).create();
        final View alertLayout = inflater.inflate(R.layout.student_found, null);
        alertDialog.setView(alertLayout);

        TextView student_name = (TextView) alertLayout.findViewById(R.id.student_name);
        TextView student_mobile = (TextView) alertLayout.findViewById(R.id.student_mobile);

        student_name.setTypeface(typeface);
        student_mobile.setTypeface(typeface);


        student_name.setText(name);
        student_mobile.setText(mobile);

        Button rescan = (Button) alertLayout.findViewById(R.id.rescan);
        Button my_ward = (Button) alertLayout.findViewById(R.id.my_ward);

        rescan.setTypeface(typeface);
        my_ward.setTypeface(typeface);

        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qr_code_reader.onResume();
                alertDialog.dismiss();
            }
        });

        my_ward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetResult("https://schoolapp-2018.herokuapp.com/parent/student/"+id+"/link");
            }
        });

        alertDialog.show();
    }

    public void GetResult(final String mURL) {
        customDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, mURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(getContext(), "Unable to submit!", Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
                params.put("Authorization","Basic "+ SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                customDialog.dismiss();
                if (response != null) {

                    if(response.statusCode == 200) {

                        success();
                        alertDialog.dismiss();
                        ((NavDashBoardActivity)context).onBackPressed();
                    }
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        addToRequestQueue(stringRequest);

    }

    public void success() {

        ((NavDashBoardActivity)context).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context,"Successfully Linked!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

}
