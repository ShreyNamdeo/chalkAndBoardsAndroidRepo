package com.android.chalkboard.school.service;

import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.SchoolListResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface AddSchoolServiceInterface {

    @POST("/admin/institution")
    Call<SchoolListResponse> createSchool(@Body AddSchoolRequest addschoolRequest);

    @PUT
    Call<Void> uploadImage(@Url String url, @Body RequestBody body);
}
