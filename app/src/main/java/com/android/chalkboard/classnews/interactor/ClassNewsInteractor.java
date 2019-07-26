package com.android.chalkboard.classnews.interactor;

import com.android.chalkboard.classnews.model.CreateClassNewsRequest;
import com.android.chalkboard.classnews.model.EditClassNewsRequest;
import com.android.chalkboard.network.NetworkListener;

import okhttp3.RequestBody;

public interface ClassNewsInteractor {

    void createClassNews(int classId, CreateClassNewsRequest body, NetworkListener listener);

    void getClassNews(int classId, NetworkListener listener);

    void deleteClassNews(int classId, int newsId, NetworkListener listener);

    void editClassNews(int classId, EditClassNewsRequest request, NetworkListener listener);

    void uploadImage(String url, RequestBody image, NetworkListener listener);
}
