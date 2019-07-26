package com.android.chalkboard.classes.service;

import com.android.chalkboard.classes.interactor.ClassInteractor;
import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassServiceClass implements ClassInteractor {

    @Override
    public void createNewClass(Class body, final NetworkListener listener){
        ClassServiceInterface classServiceInterface = new RetrofitClient().getClient().create(ClassServiceInterface.class);
        classServiceInterface.createNewClass(body).enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                try {
                    if (response.code() == 201)
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
            public void onFailure(Call<Class> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getAllClasses(final NetworkListener listener){
        ClassServiceInterface classServiceInterface = new RetrofitClient().getClient().create(ClassServiceInterface.class);
        classServiceInterface.getAllClasses().enqueue(new Callback<List<ClassResponse>>() {
            @Override
            public void onResponse(Call<List<ClassResponse>> call, Response<List<ClassResponse>> response) {
                try {
                    if (response.errorBody() == null)
                        listener.onSuccess(response.body());
                    else{
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<ClassResponse>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void deleteClass(int classId, final NetworkListener listener){
        ClassServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassServiceInterface.class);
        serviceInterface.deleteClass(classId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    if (response.code() == 200)
                        listener.onSuccess("Class has been deleted successfully.");
                    else{
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        listener.onFailure(obj.getString("message"));
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

    @Override
    public void updateClass(Class body, final NetworkListener listener){
        ClassServiceInterface serviceInterface = new RetrofitClient().getClient().create(ClassServiceInterface.class);
        serviceInterface.updateClass(body).enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                try {
                    if (response.code() == 200)
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
            public void onFailure(Call<Class> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
