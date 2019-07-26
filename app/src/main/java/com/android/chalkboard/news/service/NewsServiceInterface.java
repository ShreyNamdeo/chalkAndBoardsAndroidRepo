package com.android.chalkboard.news.service;

import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.login.modal.UserRequest;
import com.android.chalkboard.news.modal.NewsRequest;
import com.android.chalkboard.news.modal.NewsResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface NewsServiceInterface {

    @POST("/admin/institution/{institutionid}/news")
    Call<NewsResponse> createSchoolNews(@Body NewsRequest newsRequest, @Path("institutionid") int instituteId);

    @POST("/admin/class/{classid}/news")
    Call<NewsResponse> createClassNews(@Body NewsRequest newsRequest, @Path("classid") int classId);


    @GET("/admin/institution/{institutionid}/news")
    Call<List<NewsResponse>> getInstitutionLevelNews(@Path("institutionid") int instituteId);

    @DELETE("/admin/institution/{institutionid}/news/{newsId}")
    Call<Void> deleteNews(@Path("institutionid") int instituteId, @Path("newsId") int newsId);

    @PUT
    Call<Void> uploadImage(@Url String url, @Body RequestBody body);


}
