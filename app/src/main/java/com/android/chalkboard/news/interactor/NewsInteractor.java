package com.android.chalkboard.news.interactor;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.news.modal.NewsRequest;

import okhttp3.RequestBody;

public interface NewsInteractor {

    void createSchoolLevelNews(NewsRequest newsRequest, int instituteId, NetworkListener networkListener);

    void getAllInstitutionLevelNews(int institutionId, NetworkListener networkListener);

    void deleteNews(int institutionId, int newsId, NetworkListener networkListener);

    void uploadImage(String url, RequestBody image, NetworkListener listener);

    void createClassLevelNews(NewsRequest newsRequest, int classId, NetworkListener networkListener);


}
