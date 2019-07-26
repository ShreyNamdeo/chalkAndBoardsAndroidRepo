package com.android.chalkboard.login.service;

import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.login.modal.UserRequest;
import com.android.chalkboard.login.modal.UserRequestOTP;
import com.android.chalkboard.login.modal.ValidateOTP;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface LoginServiceInterface {

    @POST("/users/email/login")
    Call<LoginResponse> loginUser(@Body UserRequest userRequest);

    @PUT("/users/otp/login")
    Call<LoginResponse> loginUserWithOTP(@Body UserRequestOTP requestOTP);

    @PUT("/users/otp/validate")
    Call<LoginResponse> loginValidateOTP(@Body ValidateOTP validateOTP);
}
