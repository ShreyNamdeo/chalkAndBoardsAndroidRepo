package com.android.chalkboard.dashboard.presenter;

public interface DashboardContract {


    public interface View{


        void bindPresenter(DashboardPresenterImpl dashboardPresenter);

        void storeInstituteData(Object success);

        void storeClassesofInstitute(Object success);

        void showError(String message);

        void setRequestedInstitute(Object success);

    }

    public interface DashboardPresenter{
        void getInstitution();

        void getClasses(int instId);

        void getInstitutions(String city, String iName);

        void requestInstitutionJoin(String institutionId);

    }
}
