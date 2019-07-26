package com.android.chalkboard.teacherrequest.service;

import com.android.chalkboard.teacherrequest.modal.TeacherDetail;
import com.android.chalkboard.teacherrequest.modal.TeacherRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TeachersRequestServiceInterface {

    @GET("/admin/institution/{institutionid}/requests")
    Call<List<TeacherRequest>> getTeacherRequest(@Path("institutionid") int institutionId);

    @PUT("/admin/institution/{institutionid}/request/{requestId}/accept")
    Call acceptTeacherRequest(@Path("institutionid") int instId, @Path("requestId") int reqId);

    @PUT("/admin/institution/{institutionid}/request/{requestId}/reject")
    Call rejectTeacherRequest(@Path("institutionid") int instId, @Path("requestId") int reqId);

    @PUT("/admin/institution/{institutionid}/request")
    Call deleteTeacherRequest(@Path("institutionid") int instId);

    @GET("/teacher/profile/{teacherId}")
    Call<TeacherDetail> getTeacherDetail(@Path("teacherId") int teacherId);
}



