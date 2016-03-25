package it.polimi.dima.giftlist.data.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.net.currency.CurrencyApi;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Alessandro on 24/03/16.
 * DebugCurrencyModule contains provides methods only for the debug build.
 * It is added to the dagger graph in mutual exclusion with ReleaseCurrencyModule.
 * DebugCurrencyModule and ReleaseCurrencyModule have some provides methods with the same
 * @Named annotation, since they are added to the graph in alternative and satisfy the same injection.
 */
@Module
public class DebugCurrencyModule {

    @Provides
    @Singleton
    @Named("CurrencyRetrofit")
    Retrofit providesDebugCurrencyApiAdapter(@Named("DebugCurrencyOkHttp") OkHttpClient currencyOkHttpClient, @Named("CurrencyXml") SimpleXmlConverterFactory simpleXmlConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(CurrencyApi.BASE_URL)
                .addConverterFactory(simpleXmlConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(currencyOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("DebugCurrencyOkHttp")
    OkHttpClient providesDebugCurrencyOkHttpClient(@Named("CurrencyHttpLog") HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
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
}
