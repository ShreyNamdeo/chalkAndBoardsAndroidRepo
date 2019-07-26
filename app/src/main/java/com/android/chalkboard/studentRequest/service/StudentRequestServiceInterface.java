package com.android.chalkboard.studentRequest.service;

import com.android.chalkboard.studentRequest.model.StudentRequestResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StudentRequestServiceInterface {

    @GET("/class/{classId}/requests")
    Call<ArrayList<StudentRequestResponse>> getStudentRequest(@Path("classId") int classId);

    @PUT("/class/{classId}/request/{requestId}/accept")
    Call<Void> acceptStudentRequest(@Path("classId") int classId, @Path("requestId") int requestId);

    @PUT("/teacher/class/{classId}/request/{requestId}/reject")
    Call<Void> rejectStudentRequest(@Path("classId") int classId, @Path("requestId") int requestId);
}
