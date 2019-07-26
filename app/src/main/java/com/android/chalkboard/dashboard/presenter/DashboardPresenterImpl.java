package com.android.chalkboard.dashboard.presenter;

import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.dashboard.service.DashboardServiceClass;
import com.android.chalkboard.network.NetworkListener;
import retrofit2.Response;

public class DashboardPresenterImpl implements DashboardContract.DashboardPresenter {

    private DashboardContract.View mView;
    private DashboardServiceClass mServiceClass;


    private DashboardPresenterImpl(DashboardContract.View view, DashboardServiceClass dashboardServiceClass){
        mView = view;
        mServiceClass = dashboardServiceClass;

    }

    public static void createPresenter(DashboardContract.View view){
        DashboardPresenterImpl dashboardPresenter = new DashboardPresenterImpl(view, new DashboardServiceClass());
        view.bindPresenter(dashboardPresenter);

    }

    @Override
    public void getInstitution() {

        mServiceClass.getInstituteList(new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.storeInstituteData(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });

    }

    @Override
    public void getInstitutions(String city, String iName){
        mServiceClass.getInstitutions(city, iName, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.storeInstituteData(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }

    @Override
    public void requestInstitutionJoin(String institutionId){
        mServiceClass.requestInstitutionJoin(institutionId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.storeInstituteData(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }

    @Override
    public void getClasses(int instId) {
        mServiceClass.getClassList(instId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.storeClassesofInstitute(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }


}
