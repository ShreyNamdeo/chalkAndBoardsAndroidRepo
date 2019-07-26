package com.android.chalkboard.dashboard.service;

import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.school.model.ClassesList;
import com.android.chalkboard.school.model.SchoolListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DashboardServiceInterface {


    //get list of institution
    @GET("/admin/institution")
    Call<List<SchoolListResponse>> getInstituteList();

    @GET("/admin/institution/{institutionid}/classes")
    Call<List<ClassesList>> getClasses(@Path("institutionid") int classId);

    @GET("/institution/{city}/{institutionName}")
    Call<List<SchoolListResponse>> getInstitutions(@Path("city") String city, @Path("institutionName") String iName);

    @POST("/institution/{institutionId}/join")
    Call<Void> requestInstitutionJoin(@Path("institutionId") String institutionId);

}
