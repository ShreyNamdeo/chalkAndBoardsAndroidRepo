package com.android.chalkboard.classes.service;

import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClassServiceInterface {

    @POST("/class")
    Call<Class> createNewClass(@Body Class body);

    @GET("/teacher/class")
    Call<List<ClassResponse>> getAllClasses();

    @DELETE("/class/{classId}")
    Call<Void> deleteClass(@Path("classId") int classId);

    @PUT("/class")
    Call<Class> updateClass(@Body Class body);
}
