package com.android.chalkboard.studentStory.classes;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Url;

public class classDetails extends Fragment {

    private View view;
    TextView className,schoolName,start_time,end_time;
    int choice;
    Button cancel,join;
    LinearLayout buttons;
    private CustomDialog customDialog;

    public classDetails() {
    }

    @SuppressLint("ValidFragment")
    public classDetails(int choice) {
        this.choice = choice;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.class_details_student,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Class Details");

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        className = (TextView) view.findViewById(R.id.className);
        schoolName = (TextView) view.findViewById(R.id.schoolName);
        start_time = (TextView) view.findViewById(R.id.start_time);
        end_time = (TextView) view.findViewById(R.id.end_time);
        cancel = (Button) view.findViewById(R.id.cancel);
        join = (Button) view.findViewById(R.id.join);
        buttons = (LinearLayout) view.findViewById(R.id.buttons);

        if(choice == 2) {
            buttons.setVisibility(View.VISIBLE);
        }
        else {
            buttons.setVisibility(View.GONE);
        }

        className.setText(getArguments().getString("name"));
        schoolName.setText(getArguments().getString("school_name"));
        start_time.setText(getArguments().getString("start_time"));
        end_time.setText(getArguments().getString("end_time"));

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //join("https://schoolapp-2018.herokuapp.com/class/"+getArguments().getInt("id")+"/join");
                JoinClass joinClass = new JoinClass("https://schoolapp-2018.herokuapp.com/class/"+getArguments().getInt("id")+"/join");
                joinClass.execute();
            }
        });

        return view;
    }

    public class JoinClass extends AsyncTask<String, String, String> {

        String urlString;
        int responseCode = -1;

        JoinClass(String url) {
            urlString = url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(responseCode == 201) {
                SharedPrefUtils.storeInSharedPref(getContext(),SharedPrefUtils.School_ID,String.valueOf(getArguments().getInt("school_id")));
                SharedPrefUtils.storeInSharedPref(getContext(),SharedPrefUtils.STANDARD,String.valueOf(getArguments().getInt("standard")));
                ((NavDashBoardActivity)getActivity()).onBackPressed();
                Toast.makeText(getContext(),"Request Completed!",Toast.LENGTH_SHORT).show();
            }
            else if(responseCode == 409){
                SharedPrefUtils.storeInSharedPref(getContext(),SharedPrefUtils.School_ID,String.valueOf(getArguments().getInt("school_id")));
                SharedPrefUtils.storeInSharedPref(getContext(),SharedPrefUtils.STANDARD,String.valueOf(getArguments().getInt("standard")));
                ((NavDashBoardActivity)getActivity()).onBackPressed();
                Toast.makeText(getContext(),"Already made a request!",Toast.LENGTH_SHORT).show();
            }
            customDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            //OutputStream out = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                //urlConnection.setDoOutput(true);
                //urlConnection.setRequestProperty("Accept", "application/json");
                //urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", "Basic "+ SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
                //out = new BufferedOutputStream(urlConnection.getOutputStream());
                responseCode = urlConnection.getResponseCode();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//                writer.flush();
//                writer.close();
                //out.close();

                //urlConnection.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;
        }
    }



}
