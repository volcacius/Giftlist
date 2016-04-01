package it.polimi.dima.giftlist.data.module;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.net.ebay.EbayApi;
import it.polimi.dima.giftlist.data.net.ebay.EbaySigningInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Elena on 26/03/2016.
 */
@Module
public class ReleaseEbayModule {

    @Provides
    @Singleton
    @Named("EbayRetrofit")
    Retrofit providesReleaseEtsyApiAdapter(@Named("EtsyGson") Gson EbayGsonInstance, @Named("ReleaseEtsyOkHttp") OkHttpClient ebayOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(EbayApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(EbayGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(ebayOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("ReleaseEbayOkHttp")
    OkHttpClient providesReleaseEtsyOkHttpClient(EbaySigningInterceptor ebaySigningInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(ebaySigningInterceptor)
                .build();
    }
}
