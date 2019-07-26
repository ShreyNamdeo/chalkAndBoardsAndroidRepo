package com.android.chalkboard.news.presenter;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.news.interactor.NewsInteractor;
import com.android.chalkboard.news.modal.NewsRequest;
import com.android.chalkboard.news.modal.NewsResponse;
import com.android.chalkboard.news.service.NewsServiceClass;
import com.android.chalkboard.util.CommonUtils;

import okhttp3.RequestBody;
import retrofit2.Response;

public class CreateNewsPresenterImpl implements CreateNewsContract.Presenter {

    private CreateNewsContract.View mView;
    private NewsInteractor mInteractor;

    private CreateNewsPresenterImpl(CreateNewsContract.View view, NewsInteractor newsInteractor){
        mView = view;
        mInteractor = newsInteractor;
    }

    public static void createPresenter(CreateNewsContract.View view){
        CreateNewsPresenterImpl createNewsPresenter = new CreateNewsPresenterImpl(view, new NewsServiceClass());
        view.bindPresenter(createNewsPresenter);
    }

    @Override
    public void createSchoolNews(NewsRequest newsRequest, int id) {
        mView.showProgressBar();
        mInteractor.createSchoolLevelNews(newsRequest, id, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideProgressBar();
                if(success!=null && CommonUtils.isSucess(((Response)success).code())) {
                    NewsResponse response = (NewsResponse) ((Response)success).body();
                    mView.sendImage(response);

                }else{
                    mView.showNetworkError();
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                mView.hideProgressBar();
            }
        });
    }

    @Override
    public void createClassNews(NewsRequest newsRequest, int id) {
        mView.showProgressBar();
        mInteractor.createClassLevelNews(newsRequest, id, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideProgressBar();
                if(success!=null && CommonUtils.isSucess(((Response)success).code())) {
                    NewsResponse response = (NewsResponse) ((Response)success).body();
                    mView.sendImage(response);

                }else{
                    mView.showNetworkError();
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                mView.hideProgressBar();
            }
        });
    }

    @Override
    public void sendImageByte(RequestBody requestBody, String writeUrl) {
        mView.showProgressBar();
        mInteractor.uploadImage(writeUrl, requestBody, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideProgressBar();
                mView.postNews();
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.hideProgressBar();
            }
        });

    }
}
