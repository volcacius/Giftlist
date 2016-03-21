package it.polimi.dima.giftlist.data.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.BuildConfig;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;
import it.polimi.dima.giftlist.data.net.etsy.EtsyResultsDeserializer;
import it.polimi.dima.giftlist.data.net.etsy.EtsySigningInterceptor;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alessandro on 21/03/16.
 */
@Module
public class EtsyModule {

    @Provides
    @Singleton
    @Named("EtsyHttpLog")
    HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @Singleton
    EtsySigningInterceptor providesEtsySigningInterceptor() {
        return new EtsySigningInterceptor(BuildConfig.ETSY_API_KEY);
    }

    @Provides
    @Singleton
    @Named("EtsyOkHttp")
    OkHttpClient providesEtsyOkHttpClient(EtsySigningInterceptor etsySigningInterceptor, @Named("EtsyHttpLog") HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(etsySigningInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    EtsyResultsDeserializer providesEtsyResultsDeserializer() {
        return new EtsyResultsDeserializer();
    }

    @Provides
    @Singleton
    @Named("EtsyGson")
    Gson providesEtsyGsonInstance(EtsyResultsDeserializer etsyResultsDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<EtsyProduct>>() {}.getType(), etsyResultsDeserializer)
                .create();
    }

    @Provides
    @Singleton
    @Named("EtsyRetrofit")
    Retrofit providesEtsyApiAdapter(@Named("EtsyGson") Gson EtsyGsonInstance, @Named("EtsyOkHttp") OkHttpClient etsyOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(EtsyApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(EtsyGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(etsyOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    EtsyApi providesEtsyApi(@Named("EtsyRetrofit") Retrofit etsyApiAdapter) {
        return etsyApiAdapter.create(EtsyApi.class);
    }
}
