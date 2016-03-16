package it.polimi.dima.giftlist;

import android.app.Application;

/**
 * Created by Alessandro on 15/03/16.
 */
public class GiftlistApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                     .applicationModule(new ApplicationModule(this))
                     .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
