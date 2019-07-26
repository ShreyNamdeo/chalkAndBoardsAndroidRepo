package com.android.chalkboard.register.service;

import com.android.chalkboard.dashboard.model.Institution;
import com.android.chalkboard.register.model.User;
import com.android.chalkboard.register.model.jwt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RegisterServiceInterface {


    @PUT("/users")
    Call<User> registerUser(@Body User user);

    //@GET("jwt?mobileNumber={mobilenumber}&role={role}")
    @GET("/jwt")
    Call<jwt>getJWTToken(@Query("mobileNumber") String mobilenumber, @Query("role") String role);
}
