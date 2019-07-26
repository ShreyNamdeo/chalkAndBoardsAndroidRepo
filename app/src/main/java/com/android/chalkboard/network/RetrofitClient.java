package com.android.chalkboard.network;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.chalkboard.ChalkAndBoardApplication;
import com.android.chalkboard.util.JwtTokenSingleton;
import com.android.chalkboard.util.SharedPrefUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "https://schoolapp-2018.herokuapp.com";
    private Retrofit retrofit = null;


    public Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = SharedPrefUtils.getFromSharedPref(ChalkAndBoardApplication.getContext(), SharedPrefUtils.JWTToken);
                if (!TextUtils.isEmpty(token)){
                    Request request = chain.request().newBuilder().addHeader("Authorization","Basic "+token).build();
                    return chain.proceed(request);
                }else {
                    Request request = chain.request().newBuilder().build();
                    return chain.proceed(request);
                }
            }
        })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .build();

        if(retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
    public  Retrofit getClientWithoutAuthorization() {
        retrofit = null;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().build();
                    return chain.proceed(request);            }
        })
                .readTimeout(600, TimeUnit.SECONDS)
                .connectTimeout(600, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .build();

        if(retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


}
