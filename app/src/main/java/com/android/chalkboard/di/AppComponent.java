package com.android.chalkboard.di;

import android.app.Application;

import com.android.chalkboard.ChalkAndBoardApplication;
import com.android.chalkboard.di.moduleUtil.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        AndroidSupportInjectionModule.class

})
public interface AppComponent extends AndroidInjector<DaggerApplication> {
    void inject(ChalkAndBoardApplication app);


    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}
