package com.android.chalkboard.classagenda.service;

import com.android.chalkboard.classagenda.model.AgendaResponse;
import com.android.chalkboard.classagenda.model.CreateAgendaRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClassAgendaServiceInterface {

    @GET("/class/{classId}/agenda")
    Call<AgendaResponse> getAgenda(@Path("classId") int classId);

    @PUT("/teacher/class/{classId}/agenda")
    Call<AgendaResponse> createAgenda(@Path("classId") int classId, @Body CreateAgendaRequest request);

    @PUT("/teacher/class/{classId}/agenda")
    Call<AgendaResponse> updateAgenda(@Path("classId") int classId, @Body CreateAgendaRequest request);
}
