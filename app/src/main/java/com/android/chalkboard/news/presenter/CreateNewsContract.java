package com.android.chalkboard.news.presenter;

import com.android.chalkboard.news.modal.NewsRequest;
import com.android.chalkboard.news.modal.NewsResponse;

import okhttp3.RequestBody;

public interface CreateNewsContract {
    public interface View {
        void bindPresenter(CreateNewsPresenterImpl createNewsPresenter);
        void sendImage(NewsResponse response);
        void showProgressBar();
        void hideProgressBar();
        void postNews();

        void showNetworkError();
    }

    public interface Presenter{


        void createSchoolNews(NewsRequest newsRequest, int id);
        void createClassNews(NewsRequest newsRequest, int id);
        void sendImageByte(RequestBody image, String writeUrl);
    }
}
