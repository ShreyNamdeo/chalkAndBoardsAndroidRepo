package com.android.chalkboard.school.presenter;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.school.interactor.AddSchoolInteractor;
import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.service.AddSchoolServiceClass;

import okhttp3.RequestBody;
import retrofit2.Response;

public class AddSchoolPresenterImpl implements AddSchoolContract.AddSchoolPresenter {
    private AddSchoolInteractor addSchoolInteractor;
    private AddSchoolContract.AddSchoolView addSchoolView;

    public AddSchoolPresenterImpl(AddSchoolContract.AddSchoolView addSchoolView, AddSchoolServiceClass addSchoolInteractor) {
        this.addSchoolInteractor = addSchoolInteractor;
        this.addSchoolView = addSchoolView;
    }

    public static void createPrensenter(AddSchoolContract.AddSchoolView addSchoolView){
        AddSchoolPresenterImpl addSchoolPresenter = new AddSchoolPresenterImpl(addSchoolView, new AddSchoolServiceClass());
        addSchoolView.bindPresenter(addSchoolPresenter);
    }


    @Override
    public void addSchool(AddSchoolRequest addSchoolRequest) {
        addSchoolInteractor.createAddSchoolCall(addSchoolRequest, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                    SchoolListResponse response = (SchoolListResponse) ((Response)success).body();
                    addSchoolView.updateSchoolListCache(response);
                    if(response != null) {
                        addSchoolView.showSpinner();
                        addSchoolView.sendImage(response);
                    }
           }

            @Override
            public void onFailure(String errorMessage) {
                String str = errorMessage;
            }
        });

    }

    @Override
    public void sendImageByte(RequestBody body, String writeUrl) {
        addSchoolInteractor.uploadImage(writeUrl, body, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                Object o = success;
                addSchoolView.hideSpinner();
                addSchoolView.closeSchoolForm();
            }

            @Override
            public void onFailure(String errorMessage) {
                addSchoolView.hideSpinner();

            }
        });
    }
}
