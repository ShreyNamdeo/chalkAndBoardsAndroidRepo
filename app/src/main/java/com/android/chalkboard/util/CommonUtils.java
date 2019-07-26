package com.android.chalkboard.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static int IMAGE_REQ_CODE = 100;

    public static boolean isValidEmailAddress(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /*public static boolean isValidPhoneNumber(CharSequence target){
        String regex = "^((\\+)?(\\d{2}[-]))?(\\d{10}){1}?$";
        Pattern pattern = Pattern.compile(regex);
        return (!TextUtils.isEmpty(target) && pattern.matcher(target).matches());
    }*/

    public static boolean isValidPhoneNumber(CharSequence target){
        boolean empty = !target.toString().trim().equals("");
        boolean check91 = (target.toString().startsWith("+91") && target.toString().length() == 13);
        boolean check0 = (target.toString().startsWith("0") && target.toString().length() == 11);
        boolean checklength = target.toString().length() == 10;

        return empty && (check91 || check0 || checklength);
    }

    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    public static String loadJSONFromAsset(Context context, int fileName) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void setDefaultInstitute(Context context, int defaultInstitute){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("SELECT_INSTITUTE", defaultInstitute).apply();
    }

    public static int getSelectedInstitute(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("SELECT_INSTITUTE", 1);
    }

    public static int getSelectedClass(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("SELECTED_CLASS", 1);
    }

    public static void createDialog(ProgressDialog dialog) {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    public static boolean isSucess(int code){
        if(code==200 || code==201){
            return true;
        }
        return false;
    }
}
