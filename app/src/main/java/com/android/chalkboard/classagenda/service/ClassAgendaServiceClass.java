package com.android.chalkboard.classagenda.service;

import com.android.chalkboard.classagenda.interactor.ClassAgendaInteractor;
import com.android.chalkboard.classagenda.model.AgendaResponse;
import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassAgendaServiceClass implements ClassAgendaInteractor {

    @Override
    public void createAgenda(int classId, CreateAgendaRequest request, final NetworkListener listener){
        ClassAgendaServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassAgendaServiceInterface.class);
        serviceInterface.createAgenda(classId, request).enqueue(new Callback<AgendaResponse>() {
            @Override
            public void onResponse(Call<AgendaResponse> call, Response<AgendaResponse> response) {
                try {
                    if (response.errorBody() == null)
                        listener.onSuccess(response.body());
                    else{
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AgendaResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getAgenda(int classId, final NetworkListener listener){
        ClassAgendaServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassAgendaServiceInterface.class);
        serviceInterface.getAgenda(classId).enqueue(new Callback<AgendaResponse>() {
            @Override
            public void onResponse(Call<AgendaResponse> call, Response<AgendaResponse> response) {
                try {
                    if (response.errorBody() == null)
                        listener.onSuccess(response.body());
                    else{
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AgendaResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void updateAgenda(int classId, CreateAgendaRequest request, final NetworkListener listener){
        ClassAgendaServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassAgendaServiceInterface.class);
        serviceInterface.updateAgenda(classId, request).enqueue(new Callback<AgendaResponse>() {
            @Override
            public void onResponse(Call<AgendaResponse> call, Response<AgendaResponse> response) {
                try {
                    if (response.errorBody() == null)
                        listener.onSuccess(response.body());
                    else{
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AgendaResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
