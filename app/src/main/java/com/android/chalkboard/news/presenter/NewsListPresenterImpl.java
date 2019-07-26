package com.android.chalkboard.news.presenter;

import android.util.Log;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.news.interactor.NewsInteractor;
import com.android.chalkboard.news.modal.NewsResponse;
import com.android.chalkboard.news.service.NewsServiceClass;

import java.util.List;

import retrofit2.Response;

public class NewsListPresenterImpl implements NewsListContract.Presenter{
    private NewsListContract.View mView;
    private NewsInteractor mInteractor;

    public NewsListPresenterImpl(NewsListContract.View newsList, NewsInteractor newsServiceClass) {
        mView = newsList;
        mInteractor = newsServiceClass;
    }

    public static void createPresenter(NewsListContract.View newsList) {
        NewsListPresenterImpl newsListPresenter = new NewsListPresenterImpl(newsList, new NewsServiceClass());
        newsList.bindPresenter(newsListPresenter);
    }

    public void getNewsList(int id, String itemType){
        mView.showProgressBar();
        mInteractor.getAllInstitutionLevelNews(id, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideProgressBar();
                if(success!=null) {
                    List<NewsResponse> response = (List<NewsResponse>)((Response)success).body();
                    mView.populateListData(response);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.hideProgressBar();
                Log.d("---FAILURE---", errorMessage);
            }
        });

    }

    @Override
    public void deleteNews(int instId, int newsId) {
        mView.showProgressBar();
        mInteractor.deleteNews(instId, newsId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideProgressBar();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
