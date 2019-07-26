package com.android.chalkboard.studentStory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;
import static com.android.chalkboard.util.Constant.ID;

public class QR_Code extends Fragment {


    private View view;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    ImageView qrImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_qr__code,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("QR Code");
//        String inputValue = "Nikhil Ahuja,9098755855,101";
        String inputValue = SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.NAME_KEY)+","+
                                SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.MOBILE_KEY+","+
                                 SharedPrefUtils.getFromSharedPref(getContext(),ID));
        qrImage = (ImageView) view.findViewById(R.id.qr_code);

        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("QR_ERROR", e.toString());
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }

}
