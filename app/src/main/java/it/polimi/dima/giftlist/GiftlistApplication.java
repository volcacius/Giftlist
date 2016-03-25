package it.polimi.dima.giftlist;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import it.polimi.dima.giftlist.data.module.CurrencyModule;
import it.polimi.dima.giftlist.data.module.DebugCurrencyModule;
import it.polimi.dima.giftlist.data.module.DebugEtsyModule;
import it.polimi.dima.giftlist.data.module.EtsyModule;
import it.polimi.dima.giftlist.data.module.ReleaseCurrencyModule;
import it.polimi.dima.giftlist.data.module.ReleaseEtsyModule;
import jp.wasabeef.takt.Takt;

/**
 * Created by Alessandro on 15/03/16.
 */
public class GiftlistApplication extends Application {

    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeApplication();
    }

    @Override
    public void onTerminate() {
        terminateApplication();
        super.onTerminate();
    }

    private void initializeApplication() {
        if (BuildConfig.DEBUG) {
            applicationComponent = DaggerDebugApplicationComponent.builder()
                                        .applicationModule(new ApplicationModule(this))
                                        .debugCurrencyModule(new DebugCurrencyModule())
                                        .debugEtsyModule(new DebugEtsyModule())
                                        .etsyModule(new EtsyModule())
                                        .currencyModule(new CurrencyModule())
                                        .build();
            Takt.stock(this).play();
            BlockCanary.install(this, new AppBlockCanaryContext()).start();
            refWatcher = LeakCanary.install(this);
            Stetho.initializeWithDefaults(this);
            


        } else {
            applicationComponent = DaggerReleaseApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .releaseCurrencyModule(new ReleaseCurrencyModule())
                    .releaseEtsyModule(new ReleaseEtsyModule())
                    .etsyModule(new EtsyModule())
                    .currencyModule(new CurrencyModule())
                    .build();
        }
    }

    private void terminateApplication() {
        if (BuildConfig.DEBUG) {
            Takt.stock(this).play();
        }
    }

    //this is used to call refwatcher from stuff where it is not called by default, e.g. within fragments
    public static RefWatcher getRefWatcher(Context context) {
        GiftlistApplication application = (GiftlistApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
