package com.android.chalkboard.login.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.FontChangeCrawler;

import java.util.ArrayList;
import java.util.List;

public class loginFragment_2 extends Fragment {

    private View view;
    private TextInputEditText otpPasswordEditText;
    Button submit_otp;
    loginFragment_1 loginFragment_1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    BroadcastReceiver receiver;
    TextView otp_timer;
    boolean pause = false;

    public loginFragment_2() {
    }

    @SuppressLint("ValidFragment")
    public loginFragment_2(com.android.chalkboard.login.view.loginFragment_1 loginFragment_1) {
        this.loginFragment_1 = loginFragment_1;
    }

    @Override
    public void onPause() {
        super.onPause();
        pause = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.loginfragment2,container,false);

        otpPasswordEditText = (TextInputEditText) view.findViewById(R.id.et_password_otp);
        submit_otp = (Button) view.findViewById(R.id.submit_otp);
        otp_timer = (TextView) view.findViewById(R.id.otp_timer);

        if(checkAndRequestPermissions()) {

        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");

                    int index = message.indexOf("is");
                    String otp = message.substring(index+3,index+10);
                    Log.e("OTPPPP",otp.trim());
                    otpPasswordEditText.setText(otp.trim());
                    if(loginFragment_1.isValidEmail()) {
                        loginFragment_1.handleGetStartedClick(otpPasswordEditText.getText().toString());
                    }
                    else {
                        loginFragment_1.handleSubmitOTPClick(otpPasswordEditText.getText().toString());
                    }

                }
            }
        };

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                otp_timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                if(!pause) {
                    otp_timer.setText("Retry");
                    otp_timer.setTextColor(getResources().getColor(R.color.themeGreen));
                    otp_timer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loginFragment_1.handleGetOTPClick();
                        }
                    });
                }
            }

        }.start();



        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginFragment_1.isValidEmail()) {
                    loginFragment_1.handleGetStartedClick(otpPasswordEditText.getText().toString());
                }
                else {
                    loginFragment_1.handleSubmitOTPClick(otpPasswordEditText.getText().toString());
                }

            }
        });

        return view;
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }
}
