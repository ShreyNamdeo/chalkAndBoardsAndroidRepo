package com.android.chalkboard.news.presenter;

import com.android.chalkboard.news.modal.NewsResponse;

import java.util.List;

public interface NewsListContract {
    public interface View{

        void bindPresenter(NewsListPresenterImpl newsListPresenter);

        void populateListData(List<NewsResponse> response);

        void showProgressBar();

        void hideProgressBar();
    }

    public interface Presenter{

        void getNewsList(int instituteId, String itemType);
        void deleteNews(int instituteId,int newsId);
    }
}
