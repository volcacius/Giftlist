package it.polimi.dima.giftlist.data.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.net.ebay.EbayApi;
import it.polimi.dima.giftlist.data.net.ebay.EbaySigningInterceptor;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Elena on 26/03/2016.
 */
@Module
public class DebugEbayModule {

    @Provides
    @Singleton
    @Named("EbayRetrofit")
    Retrofit providesDebugEbayApiAdapter(@Named("EbayGson") Gson EbayGsonInstance, @Named("DebugEbayOkHttp") OkHttpClient ebayOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(EbayApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(EbayGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(ebayOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("DebugEbayOkHttp")
    OkHttpClient providesDebugEbayOkHttpClient(EbaySigningInterceptor ebaySigningInterceptor, @Named("EbayHttpLog") HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(ebaySigningInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Singleton
    @Named("EbayHttpLog")
    HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}

