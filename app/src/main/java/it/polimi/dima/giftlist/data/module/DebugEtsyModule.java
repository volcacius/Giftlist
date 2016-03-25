package it.polimi.dima.giftlist.data.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;
import it.polimi.dima.giftlist.data.net.etsy.EtsySigningInterceptor;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alessandro on 24/03/16.
 * DebugEtsyModule contains provides methods only for the debug build.
 * It is added to the dagger graph in mutual exclusion with ReleaseEtsyModule.
 * DebugEtsyModule and ReleaseEtsyModule have some provides methods with the same
 * @Named annotation, since they are added to the graph in alternative and satisfy the same injection.
 */
@Module
public class DebugEtsyModule {

    @Provides
    @Singleton
    @Named("EtsyRetrofit")
    Retrofit providesDebugEtsyApiAdapter(@Named("EtsyGson") Gson EtsyGsonInstance, @Named("DebugEtsyOkHttp") OkHttpClient etsyOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(EtsyApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(EtsyGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(etsyOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("DebugEtsyOkHttp")
    OkHttpClient providesDebugEtsyOkHttpClient(EtsySigningInterceptor etsySigningInterceptor, @Named("EtsyHttpLog") HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(etsySigningInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Singleton
    @Named("EtsyHttpLog")
    HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
