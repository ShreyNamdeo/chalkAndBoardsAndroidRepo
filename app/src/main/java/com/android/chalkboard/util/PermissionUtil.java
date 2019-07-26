package com.android.chalkboard.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.android.chalkboard.dashboard.view.NavDashBoardActivity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class PermissionUtil {

    private static final int STORAGE_PERMISSION_REQ_CODE = 200;

    public static boolean checkPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity) {

        ActivityCompat.requestPermissions(activity, new String[]{READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQ_CODE);

    }
}
