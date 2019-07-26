package com.android.chalkboard;

import android.app.Activity;
import android.content.Context;


import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class ChalkAndBoardApplication extends android.support.multidex.MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    public static Context getContext() {
        return context;
    }

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
      context = this;


    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

}
