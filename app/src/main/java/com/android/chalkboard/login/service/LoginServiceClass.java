package com.android.chalkboard.login.service;

import com.android.chalkboard.login.interactor.LoginInteractor;
import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.login.modal.UserRequest;
import com.android.chalkboard.login.modal.UserRequestOTP;
import com.android.chalkboard.login.modal.ValidateOTP;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginServiceClass implements LoginInteractor, Callback {

    private NetworkListener mListener;
    private LoginServiceInterface loginInterface;

    @Override
    public void createLoginCall(Object request, NetworkListener listener) {
        mListener = listener;
        loginInterface = new RetrofitClient().getClient().create(LoginServiceInterface.class);
        if(request instanceof UserRequest){
            Call<LoginResponse> userResponseCall = loginInterface.loginUser((UserRequest)request);
            userResponseCall.enqueue(this);
        }else if(request instanceof UserRequestOTP){
            Call<LoginResponse> userResponseCall = loginInterface.loginUserWithOTP((UserRequestOTP)request);
            userResponseCall.enqueue(this);
        }else if(request instanceof ValidateOTP){
            Call<LoginResponse> userResponseCall = loginInterface.loginValidateOTP((ValidateOTP)request);
            userResponseCall.enqueue(this);
        }

    }

   /* @Override
    public void createLoginWithOTP(UserRequestOTP requestOTP, NetworkListener listener) {
        LoginServiceInterface loginInterface = RetrofitClient.getClient().create(LoginServiceInterface.class);
        Call<UserRequestOTP> userResponseCall = loginInterface.loginUserWithOTP(requestOTP);
        userResponseCall.enqueue(new Callback<UserRequestOTP>() {
            @Override
            public void onResponse(Call<UserRequestOTP> call, Response<UserRequestOTP> response) {

            }

            @Override
            public void onFailure(Call<UserRequestOTP> call, Throwable t) {

            }
        });
    }

    @Override
    public void createLoginValidateOTP(ValidateOTP validateOTP, NetworkListener listener) {
        LoginServiceInterface loginInterface = RetrofitClient.getClient().create(LoginServiceInterface.class);
        Call<ValidateOTP> userResponseCall = loginInterface.loginValidateOTP(validateOTP);
        userResponseCall.enqueue(new Callback<ValidateOTP>() {
            @Override
            public void onResponse(Call<ValidateOTP> call, Response<ValidateOTP> response) {

            }

            @Override
            public void onFailure(Call<ValidateOTP> call, Throwable t) {

            }
        });
    }

    @Override
    public void createLoginWithPassword(UserRequest request, NetworkListener listener) {
        LoginServiceInterface loginInterface = RetrofitClient.getClient().create(LoginServiceInterface.class);
        Call<UserRequest> userResponseCall = loginInterface.loginUser(request);
        userResponseCall.enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {

            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable t) {

            }
        });
    }*/


    @Override
    public void onResponse(Call call, Response response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        mListener.onFailure(t.getMessage());
    }
}
