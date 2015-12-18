package it.polimi.dima.giftlist.injection.component;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.data.DataManager;
import it.polimi.dima.giftlist.data.SyncService;
import it.polimi.dima.giftlist.data.local.DatabaseHelper;
import it.polimi.dima.giftlist.data.local.PreferencesHelper;
import it.polimi.dima.giftlist.data.remote.RibotsService;
import it.polimi.dima.giftlist.injection.ApplicationContext;
import it.polimi.dima.giftlist.injection.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    RibotsService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    Bus eventBus();

}
