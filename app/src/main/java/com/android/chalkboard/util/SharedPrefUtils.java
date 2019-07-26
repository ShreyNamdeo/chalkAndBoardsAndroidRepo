package com.android.chalkboard.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.school.model.ClassesList;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SharedPrefUtils {

    private static SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String ROLE = "role";
    public static final String JWTToken = "jwt";
    public static final String MOBILE_KEY = "mobile";
    public static final String NAME_KEY = "name";
    public static final String PIC_KEY = "pic";
    public static final String EMAIL_KEY = "email";
    public static final String GET_INSTITUTE = "get_institute";
    public static final String GET_CLASSES = "get_class";
    public static final String GET_REQUESTED_INSTITUTE = "get_requested_institute";
    public static final String School_ID = "school_id";
    public static final String STANDARD = "standard";

    @Inject
    public SharedPrefUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
    }

    public static void storeInSharedPref(Context context, String key, String value) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getFromSharedPref(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");

    }

    public static void storeInstitituteObject(Context context, List<SchoolListResponse> myObj) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = new Gson().toJson(myObj);
        prefsEditor.putString(GET_INSTITUTE, json);
        prefsEditor.commit();
    }



    public static ArrayList<SchoolListResponse> retrieveInstituteList(Context context) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(GET_INSTITUTE, "");
        Type listType = new TypeToken<ArrayList<SchoolListResponse>>() {
        }.getType();
        return new Gson().fromJson(json, listType);

    }

    public static void storeRequestedInstituteObject(Context context, List<SchoolListResponse> myObj) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = new Gson().toJson(myObj);
        prefsEditor.putString(GET_REQUESTED_INSTITUTE, json);
        prefsEditor.commit();
    }

    public static void storeClassObject(Context context, ArrayList<ClassResponse> myObj) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = new Gson().toJson(myObj);
        prefsEditor.putString(GET_REQUESTED_INSTITUTE, json);
        prefsEditor.commit();
    }

    public static ArrayList<ClassResponse> retrieveClassList(Context context) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(GET_REQUESTED_INSTITUTE, "");
        Type listType = new TypeToken<ArrayList<ClassResponse>>() {
        }.getType();
        return new Gson().fromJson(json, listType);

    }

    public static ArrayList<SchoolListResponse> retrieveRequestedInstituteList(Context context) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(GET_REQUESTED_INSTITUTE, "");
        Type listType = new TypeToken<ArrayList<SchoolListResponse>>() {
        }.getType();
        return new Gson().fromJson(json, listType);

    }


    public static void storeClasses(Context context, List<ClassesList> classesLists){
//        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = new Gson().toJson(classesLists);
        prefsEditor.putString(GET_CLASSES, json);
        prefsEditor.commit();
    }

    public static ArrayList<ClassesList> getClasses(Context context){
//        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(GET_CLASSES, "");
        Type listType = new TypeToken<ArrayList<ClassesList>>() {
        }.getType();
        return new Gson().fromJson(json, listType);
    }


    public static void clearSharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.commit();

    }


}
