package it.polimi.dima.giftlist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by Alessandro on 15/03/16.
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @Named("AppContext")
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    @Named("MainThread")
    Scheduler provideMainThread() {
        return AndroidSchedulers.mainThread();
    }


    @Provides
    @Singleton
    Picasso providesPicasso() {
        return Picasso.with(application);
    }
}
