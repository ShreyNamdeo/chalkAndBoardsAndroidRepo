package com.android.chalkboard.dashboard.interactor;

import com.android.chalkboard.network.NetworkListener;

public interface DashboardInteractor {

    void getInstituteList(NetworkListener listener);

    void getClassList(int instId, NetworkListener listener);

    void getInstitutions(String city, String iName, NetworkListener listener);

    void requestInstitutionJoin(String institutionId, NetworkListener listener);

}
