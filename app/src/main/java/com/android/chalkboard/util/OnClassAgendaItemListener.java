package com.android.chalkboard.util;

import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.classes.model.ClassResponse;

public interface OnClassAgendaItemListener {

    void createAgenda(CreateAgendaRequest request, ClassResponse response);

    void updateAgenda(CreateAgendaRequest request, ClassResponse response);
}
