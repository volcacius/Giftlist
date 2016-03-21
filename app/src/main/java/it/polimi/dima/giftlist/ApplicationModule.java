package it.polimi.dima.giftlist;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.BuildConfig;
import it.polimi.dima.giftlist.UIThread;
import it.polimi.dima.giftlist.data.DummyInterface;
import it.polimi.dima.giftlist.data.DummyList;
import it.polimi.dima.giftlist.data.net.currency.CurrencyApi;
import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.data.executor.JobExecutor;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;
import it.polimi.dima.giftlist.data.net.etsy.EtsyResultsDeserializer;
import it.polimi.dima.giftlist.data.net.etsy.EtsySigningInterceptor;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

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
    public ProductRepository providesRepository(EtsyApi etsyApi, CurrencyDataSource currencyDataSource, EventBus eventBus) {
        return new EtsyProductDataSource(etsyApi, currencyDataSource, eventBus);
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
