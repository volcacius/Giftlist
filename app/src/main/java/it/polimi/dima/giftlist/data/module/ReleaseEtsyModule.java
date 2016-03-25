package it.polimi.dima.giftlist.data.module;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;
import it.polimi.dima.giftlist.data.net.etsy.EtsySigningInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alessandro on 24/03/16.
 * ReleaseEtsyModule contains provides methods only for the debug build.
 * It is added to the dagger graph in mutual exclusion with DebugEtsyModule.
 * DebugEtsyModule and ReleaseEtsyModule must have provides methods with the same
 * @Named annotation, since they are added to the graph in alternative and satisfy the same injection.
 */
@Module
public class ReleaseEtsyModule {

    @Provides
    @Singleton
    @Named("EtsyRetrofit")
    Retrofit providesReleaseEtsyApiAdapter(@Named("EtsyGson") Gson EtsyGsonInstance, @Named("ReleaseEtsyOkHttp") OkHttpClient etsyOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(EtsyApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(EtsyGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(etsyOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("ReleaseEtsyOkHttp")
    OkHttpClient providesReleaseEtsyOkHttpClient(EtsySigningInterceptor etsySigningInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(etsySigningInterceptor)
                .build();
    }
}
