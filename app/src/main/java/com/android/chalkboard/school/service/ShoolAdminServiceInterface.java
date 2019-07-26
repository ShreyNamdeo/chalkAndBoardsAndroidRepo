package com.android.chalkboard.school.service;

import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.SchoolListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoolAdminServiceInterface {

    @GET("/admin/institution")
    Call<ArrayList<SchoolListResponse>>getSchoolList();

    @DELETE("/admin/{institutionid}/institution")
    Call<Void> deleteInstitute(@Path("institutionid") String instituteId);

    @GET("/teacher/{teacherId}/institutions/joined")
    Call<ArrayList<SchoolListResponse>> getRequestedInstitute(@Path("teacherId") String teacherId);

    @DELETE("/teacher/institutions/{institutionId}/remove")
    Call<SimpleResponseMessage> deleteTeacherRequestedInstitute(@Path("institutionId") String instituteId);
}
