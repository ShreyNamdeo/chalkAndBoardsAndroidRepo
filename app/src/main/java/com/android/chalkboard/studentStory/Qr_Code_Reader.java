package com.android.chalkboard.studentStory;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.SharedPrefUtils;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.android.chalkboard.util.Constant.ID;

public class Qr_Code_Reader extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private CustomDialog customDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mScannerView = new ZXingScannerView(getContext());
        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Scan QR");

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Log.e("QR_RESULT",rawResult.getText());

        String[] result = rawResult.getText().split(",");

        String name = result[0];
        String mobile = result[1];
        int student_id = Integer.parseInt(result[2]);

        Student_found_Dialog student_found_dialog = new Student_found_Dialog(getContext(),name,mobile,student_id,this,customDialog);
        student_found_dialog.show(getLayoutInflater());

    }   // implements QRCodeReaderView.OnQRCodeReadListener

//    private View view;
//    QRCodeReaderView qrCodeReaderView;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        view = inflater.inflate(R.layout.fragment_qr__code__reader,container,false);
//
//        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Scan QR");
//
//
//        qrCodeReaderView = (QRCodeReaderView) view.findViewById(R.id.qrdecoderview);
//        qrCodeReaderView.setOnQRCodeReadListener(this);
//
//        qrCodeReaderView.setQRDecodingEnabled(true);
//        qrCodeReaderView.setAutofocusInterval(2000L);
//        qrCodeReaderView.setBackCamera();
//        qrCodeReaderView.animate();
//
//      return view;
//    }
//
//    @Override
//    public void onQRCodeRead(String text, PointF[] points) {
//        Log.e("QR_RESULT",text);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        qrCodeReaderView.startCamera();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        qrCodeReaderView.stopCamera();
//    }


}
