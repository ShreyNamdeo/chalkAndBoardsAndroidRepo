package com.android.chalkboard.register.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.register.interactor.RegistrationInteractor;
import com.android.chalkboard.register.model.User;
import com.android.chalkboard.register.model.jwt;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterServiceClass implements RegistrationInteractor {

    @Override
    public void createRegisterCall(User user, final NetworkListener listener) {
        RegisterServiceInterface registerServiceInterface = new RetrofitClient().getClient().create(RegisterServiceInterface.class);
        Call<User> userResponseCall = registerServiceInterface.registerUser(user);
        userResponseCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("--RESPONSE--ON ERORR--","---"+t.toString());
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getJWTToken(String mobilenumber, String role, final NetworkListener listener){
        RegisterServiceInterface registerServiceInterface = new RetrofitClient().getClient().create(RegisterServiceInterface.class);
        Call<jwt> jwtCall = registerServiceInterface.getJWTToken(mobilenumber, role);
        jwtCall.enqueue(new Callback<jwt>() {
            @Override
            public void onResponse(Call<jwt> call, Response<jwt> response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<jwt> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
