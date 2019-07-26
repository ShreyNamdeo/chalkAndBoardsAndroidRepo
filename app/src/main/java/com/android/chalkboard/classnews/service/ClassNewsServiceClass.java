package com.android.chalkboard.classnews.service;

import android.util.Log;

import com.android.chalkboard.classnews.interactor.ClassNewsInteractor;
import com.android.chalkboard.classnews.model.CreateClassNewsRequest;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;
import com.android.chalkboard.classnews.model.EditClassNewsRequest;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassNewsServiceClass implements ClassNewsInteractor {

    @Override
    public void createClassNews(int classId, CreateClassNewsRequest body, final NetworkListener listener){
        ClassNewsServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassNewsServiceInterface.class);
        serviceInterface.createClassNews(classId, body).enqueue(new Callback<CreateClassNewsResponse>() {
            @Override
            public void onResponse(Call<CreateClassNewsResponse> call, Response<CreateClassNewsResponse> response) {
                try {
                    if (response.code() == 200)
                        listener.onSuccess(response.body());
                    else {
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CreateClassNewsResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void editClassNews(int classId, EditClassNewsRequest request, final NetworkListener listener){
        ClassNewsServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassNewsServiceInterface.class);
        serviceInterface.editClassNews(classId, request).enqueue(new Callback<CreateClassNewsResponse>() {
            @Override
            public void onResponse(Call<CreateClassNewsResponse> call, Response<CreateClassNewsResponse> response) {
                try {
                    if (response.code() == 200) {
                        listener.onSuccess(response.body());
                    }
                    else {
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CreateClassNewsResponse> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void deleteClassNews(int classId, int newsId, final NetworkListener listener){
        ClassNewsServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassNewsServiceInterface.class);
        serviceInterface.deleteClassNews(classId, newsId).enqueue(new Callback<SimpleResponseMessage>() {
            @Override
            public void onResponse(Call<SimpleResponseMessage> call, Response<SimpleResponseMessage> response) {
                try {
                    if (response.errorBody() != null) {
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    } else {
                        listener.onSuccess(response.body());
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponseMessage> call, Throwable t) {

            }
        });
    }

    @Override
    public void getClassNews(int classId, final NetworkListener listener){
        ClassNewsServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassNewsServiceInterface.class);
        serviceInterface.getClassNews(classId).enqueue(new Callback<ArrayList<CreateClassNewsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CreateClassNewsResponse>> call, Response<ArrayList<CreateClassNewsResponse>> response) {
                try {
                    if (response.code() == 200)
                        listener.onSuccess(response.body());
                    else {
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CreateClassNewsResponse>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void uploadImage(String url, RequestBody bodyPart, final NetworkListener listener) {
       ClassNewsServiceInterface serviceInterface = new RetrofitClient().getClientWithoutAuthorization().create(ClassNewsServiceInterface.class);
       serviceInterface.uploadImage(url, bodyPart).enqueue(new Callback<Void>() {
           @Override
           public void onResponse(Call<Void> call, Response<Void> response) {
               try {
                   if (response.code() == 200)
                       listener.onSuccess(response);
                   else {
                        listener.onFailure(response.errorBody().string());
                   }
               } catch(Exception e){
                   listener.onFailure(e.getMessage());
               }
           }

           @Override
           public void onFailure(Call<Void> call, Throwable t) {
               listener.onFailure(t.getMessage());
           }
       });
    }
}
