package com.android.chalkboard.classes.interactor;

import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.network.NetworkListener;

public interface ClassInteractor {

    void createNewClass(Class body, NetworkListener listener);

    void getAllClasses(NetworkListener listener);

    void deleteClass(int classId, NetworkListener listener);

    void updateClass(Class body, NetworkListener listener);
}
