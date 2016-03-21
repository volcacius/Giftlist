package it.polimi.dima.giftlist.data.module;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.net.currency.CurrencyApi;
import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Alessandro on 21/03/16.
 */
@Module
public class CurrencyModule {

    @Provides
    @Singleton
    @Named("CurrencyRetrofit")
    Retrofit providesCurrencyApiAdapter(@Named("CurrencyOkHttp") OkHttpClient currencyOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(CurrencyApi.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(currencyOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("CurrencyHttpLog")
    HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @Singleton
    CurrencyApi providesCurrencyApi(@Named("CurrencyRetrofit") Retrofit currencyApiAdapter) {
        return currencyApiAdapter.create(CurrencyApi.class);
    }

    @Provides
    @Singleton
    CurrencyDataSource providesCurrencyDataSource(CurrencyApi currencyApi, EventBus eventBus) {
        return new CurrencyDataSource(currencyApi, eventBus);
    }

    @Provides
    @Singleton
    @Named("CurrencyOkHttp")
    OkHttpClient providesCurrencyOkHttpClient(@Named("CurrencyHttpLog") HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }
}
