package com.android.chalkboard.news.service;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.news.interactor.NewsInteractor;
import com.android.chalkboard.news.modal.NewsRequest;
import com.android.chalkboard.news.modal.NewsResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsServiceClass implements NewsInteractor {

    @Override
    public void createSchoolLevelNews(NewsRequest newsRequest, int institutionId, final NetworkListener networkListener) {
        NewsServiceInterface newsServiceInterface = new RetrofitClient().getClient().create(NewsServiceInterface.class);
        Call<NewsResponse> newsResponseCall = newsServiceInterface.createSchoolNews(newsRequest, institutionId);
        newsResponseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                networkListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                networkListener.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void getAllInstitutionLevelNews(int institutionId, final NetworkListener networkListener) {
        NewsServiceInterface newsServiceInterface = new RetrofitClient().getClient().create(NewsServiceInterface.class);
        Call<List<NewsResponse>> newsResponseCall = newsServiceInterface.getInstitutionLevelNews(institutionId);
        newsResponseCall.enqueue(new retrofit2.Callback<List<NewsResponse>>(){

            @Override
            public void onResponse(Call<List<NewsResponse>> call, Response<List<NewsResponse>> response) {
                networkListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<List<NewsResponse>> call, Throwable t) {
                networkListener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void deleteNews(int institutionId, int newsId, final NetworkListener networkListener) {
        NewsServiceInterface newsServiceInterface = new RetrofitClient().getClient().create(NewsServiceInterface.class);
        Call<Void> newsResponseCall = newsServiceInterface.deleteNews(institutionId, newsId);
        newsResponseCall.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                networkListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                networkListener.onFailure(t.getMessage());
            }
        });
    }


    @Override
    public void uploadImage(String url, RequestBody requestBody, final NetworkListener listener) {
        NewsServiceInterface newsServiceInterface = new RetrofitClient().getClientWithoutAuthorization().create(NewsServiceInterface.class);
        Call<Void> uploadImage = newsServiceInterface.uploadImage(url, requestBody);
        uploadImage.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void createClassLevelNews(NewsRequest newsRequest, int classId, final NetworkListener networkListener) {
        NewsServiceInterface newsServiceInterface = new RetrofitClient().getClient().create(NewsServiceInterface.class);
        Call<NewsResponse> newsResponseCall = newsServiceInterface.createClassNews(newsRequest, classId);
        newsResponseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                networkListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                networkListener.onFailure(t.getMessage());
            }
        });

    }
}
