package com.android.chalkboard.classagenda.interactor;

import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.network.NetworkListener;

public interface ClassAgendaInteractor {
    void createAgenda(int classId, CreateAgendaRequest request, NetworkListener listener);

    void updateAgenda(int classId, CreateAgendaRequest request, NetworkListener listener);

    void getAgenda(int classId, NetworkListener listener);
}
