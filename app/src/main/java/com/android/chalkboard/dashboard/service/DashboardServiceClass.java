package com.android.chalkboard.dashboard.service;

import com.android.chalkboard.dashboard.interactor.DashboardInteractor;
import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.school.model.ClassesList;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import java.util.List;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardServiceClass implements DashboardInteractor {
    @Override
    public void getInstituteList(final NetworkListener listener) {

        DashboardServiceInterface serviceInterface = new RetrofitClient().getClient().create(DashboardServiceInterface.class);
        serviceInterface.getInstituteList().enqueue(new Callback<List<SchoolListResponse>>() {
            @Override
            public void onResponse(Call<List<SchoolListResponse>> call, Response<List<SchoolListResponse>> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<SchoolListResponse>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void getInstitutions(String city, String iName, final NetworkListener listener){
        DashboardServiceInterface serviceInterface = new RetrofitClient().getClient().create(DashboardServiceInterface.class);
        Call<List<SchoolListResponse>> sList = serviceInterface.getInstitutions(city, iName);
        sList.enqueue(new Callback<List<SchoolListResponse>>() {
            @Override
            public void onResponse(Call<List<SchoolListResponse>> call, Response<List<SchoolListResponse>> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<SchoolListResponse>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void requestInstitutionJoin(String institutionId, final NetworkListener listener){
        DashboardServiceInterface serviceInterface = new RetrofitClient().getClient().create(DashboardServiceInterface.class);
        serviceInterface.requestInstitutionJoin(institutionId);
        Call<Void> response = serviceInterface.requestInstitutionJoin(institutionId);
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    System.out.println("Code:" + response.code());
                    if(response.code() == 201) {
                        System.out.println("Code: 201");
                        listener.onSuccess(true);
                    }
                    else if (response.errorBody() != null) {
                        String r = response.errorBody().string();
                        JSONObject obj = new JSONObject(r);
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getClassList(int instId, final NetworkListener listener) {
        DashboardServiceInterface serviceInterface = new RetrofitClient().getClient().create(DashboardServiceInterface.class);
        serviceInterface.getClasses(instId).enqueue(new Callback<List<ClassesList>>() {
            @Override
            public void onResponse(Call<List<ClassesList>> call, Response<List<ClassesList>> response) {
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<ClassesList>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
