package it.polimi.dima.giftlist;

import android.app.Application;

import it.polimi.dima.giftlist.presentation.component.ApplicationComponent;
import it.polimi.dima.giftlist.presentation.component.DaggerApplicationComponent;
import it.polimi.dima.giftlist.presentation.module.ApplicationModule;

/**
 * Created by Alessandro on 15/03/16.
 */
public class GiftlistApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                     .applicationModule(new ApplicationModule(this))
                     .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
