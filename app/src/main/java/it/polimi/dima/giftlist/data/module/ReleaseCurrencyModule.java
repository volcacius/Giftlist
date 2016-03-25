package it.polimi.dima.giftlist.data.module;

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
 * ReleaseCurrencyModule contains provides methods only for the debug build.
 * It is added to the dagger graph in mutual exclusion with DebugCurrencyModule.
 * DebugCurrencyModule and ReleaseCurrencyModule have some provides methods with the same
 * @Named annotation, since they are added to the graph in alternative and satisfy the same injection.
 */
@Module
public class ReleaseCurrencyModule {

    @Provides
    @Singleton
    @Named("CurrencyRetrofit")
    Retrofit providesReleaseCurrencyApiAdapter(@Named("ReleaseCurrencyOkHttp") OkHttpClient currencyOkHttpClient, @Named("CurrencyXml") SimpleXmlConverterFactory simpleXmlConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(CurrencyApi.BASE_URL)
                .addConverterFactory(simpleXmlConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(currencyOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("ReleaseCurrencyOkHttp")
    OkHttpClient providesReleaseCurrencyOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
