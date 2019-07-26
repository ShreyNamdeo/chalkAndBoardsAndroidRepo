package com.android.chalkboard.school.presenter;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.school.interactor.EditSchoolInteractor;
import com.android.chalkboard.school.model.EditSchoolRequest;
import com.android.chalkboard.school.service.EditSchoolServiceClass;

public class EditSchoolPresenterImpl implements EditSchoolContract.EditSchoolPresenter {

    private EditSchoolInteractor editSchoolInteractor;
    private EditSchoolContract.EditSchoolView editSchoolView;

    public EditSchoolPresenterImpl(EditSchoolContract.EditSchoolView editSchoolView, EditSchoolServiceClass editSchoolInteractor) {
        this.editSchoolInteractor = editSchoolInteractor;
        this.editSchoolView = editSchoolView;
    }

    public static void createPrensenter(EditSchoolContract.EditSchoolView editSchoolView){
        EditSchoolPresenterImpl editSchoolPresenter = new EditSchoolPresenterImpl(editSchoolView, new EditSchoolServiceClass());
        editSchoolView.bindPresenter(editSchoolPresenter);
    }

    @Override
    public void editSchool(EditSchoolRequest editSchoolRequest) {
        editSchoolView.showSpinner();
        editSchoolInteractor.createEditSchoolCall(editSchoolRequest, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                editSchoolView.closeSchoolForm();
                editSchoolView.hideSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }
}
