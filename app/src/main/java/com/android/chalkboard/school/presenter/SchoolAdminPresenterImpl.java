package com.android.chalkboard.school.presenter;

import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.school.interactor.SchoolAdminInteractor;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.service.SchoolAdminServiceClass;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Response;

public class SchoolAdminPresenterImpl implements SchoolAdminContract.SchoolAdminPresenter {

    private SchoolAdminInteractor schoolAdminInteractor;
    private SchoolAdminContract.SchoolAdminView schoolAdminView ;

    public SchoolAdminPresenterImpl(SchoolAdminContract.SchoolAdminView schoolAdminView, SchoolAdminServiceClass schoolAdminServiceClass) {
        this.schoolAdminView = schoolAdminView;
        schoolAdminInteractor = schoolAdminServiceClass;
    }

    public static void  createPresenter(SchoolAdminContract.SchoolAdminView schoolAdminView){
        SchoolAdminPresenterImpl schoolAdminPresenter = new SchoolAdminPresenterImpl(schoolAdminView, new SchoolAdminServiceClass());
        schoolAdminView.bindPresenter(schoolAdminPresenter);
    }

    @Override
    public void loadSchoolList() {
        schoolAdminView.showSpinner();
        schoolAdminInteractor.getSchoolListCall(new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                ArrayList<SchoolListResponse> schoolListResponses = (ArrayList<SchoolListResponse>)((Response) success).body();
                schoolAdminView.loadSchoolList(schoolListResponses);
                schoolAdminView.hideSpinner();

            }

            @Override
            public void onFailure(String errorMessage) {
                //String s = errorMessage;
                schoolAdminView.showError(errorMessage);
            }
        });

    }

    @Override
    public void deleteTeacherRequestedInstitute(String instituteId){
        schoolAdminView.showSpinner();
        schoolAdminInteractor.deleteTeacherRequestedInstitute(instituteId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                try {
                    if (((Response) success).errorBody() == null) {
                        SimpleResponseMessage msg = (SimpleResponseMessage)((Response) success).body();
                        if(msg.getMessage().equalsIgnoreCase("true"))
                            schoolAdminView.teacherInstituteDeleted(true);
                        else
                            schoolAdminView.teacherInstituteDeleted(false);
                        schoolAdminView.hideSpinner();
                    } else {
                        JSONObject j = new JSONObject(((Response) success).errorBody().string());
                        schoolAdminView.showError(j.getString("message"));
                    }
                } catch(Exception e){
                    schoolAdminView.showError(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void getRequestedInstituteList(String teacherId){
        schoolAdminView.showSpinner();
        schoolAdminInteractor.getRequestedSchoolListCall(teacherId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                try {
                    if (((Response) success).errorBody() == null) {
                        ArrayList<SchoolListResponse> schoolListResponses = (ArrayList<SchoolListResponse>) ((Response) success).body();
                        schoolAdminView.loadSchoolList(schoolListResponses);
                        schoolAdminView.hideSpinner();
                    } else {
                        JSONObject j = new JSONObject(((Response) success).errorBody().string());
                        schoolAdminView.showError(j.getString("message"));
                    }
                } catch(Exception e){
                    schoolAdminView.showError(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }



    @Override
    public void deleteInstitute(String id) {
        schoolAdminInteractor.deleteSchoolCall(id, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                schoolAdminView.instituteDeleted();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
