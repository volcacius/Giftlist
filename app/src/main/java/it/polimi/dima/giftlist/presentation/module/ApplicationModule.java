package it.polimi.dima.giftlist.presentation.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.UIThread;
import it.polimi.dima.giftlist.data.DummyInterface;
import it.polimi.dima.giftlist.data.DummyList;
import it.polimi.dima.giftlist.data.converter.CurrencyDownloader;
import it.polimi.dima.giftlist.data.executor.JobExecutor;
import it.polimi.dima.giftlist.data.repository.ProductRepository;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.scope.PerActivity;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

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
    public CurrencyDownloader providesCurrencyDownloader() {
        return new CurrencyDownloader();
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    public ProductRepository providesRepository() {
        return new EtsyProductDataSource();
    }

    @Provides
    @Singleton
    public DummyInterface providesDummyInterface() {
        return new DummyList();
    }

    @Provides
    @Singleton
    public ErrorMessageDeterminer providesErrorMessageDeterminer(){
        return new ErrorMessageDeterminer();
    }

    @Provides
    @Singleton
    IntentStarter providesIntentStarter() {
        return new IntentStarter();
    }
}
