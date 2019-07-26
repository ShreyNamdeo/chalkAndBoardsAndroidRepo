package com.android.chalkboard.classnews.service;

import com.android.chalkboard.classnews.model.CreateClassNewsRequest;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;
import com.android.chalkboard.classnews.model.EditClassNewsRequest;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface ClassNewsServiceInterface {

    @POST("/teacher/class/{classId}/news")
    Call<CreateClassNewsResponse> createClassNews(@Path("classId") int classId, @Body CreateClassNewsRequest request);

    @GET("/teacher/class/{classId}/news")
    Call<ArrayList<CreateClassNewsResponse>> getClassNews(@Path("classId") int classId);

    @DELETE("/teacher/class/{classId}/news/{newsId}")
    Call<SimpleResponseMessage> deleteClassNews(@Path("classId") int classId, @Path("newsId") int newsId);

    @PUT("/teacher/class/{classId}/news")
    Call<CreateClassNewsResponse> editClassNews(@Path("classId") int classId, @Body EditClassNewsRequest request);

    @PUT
    Call<Void> uploadImage(@Url String url, @Body RequestBody body);
}
