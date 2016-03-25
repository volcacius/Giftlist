package it.polimi.dima.giftlist.data.module;

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
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;
import it.polimi.dima.giftlist.data.net.etsy.EtsyResultsDeserializer;
import it.polimi.dima.giftlist.data.net.etsy.EtsySigningInterceptor;
import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alessandro on 21/03/16.
 * EtsyModule contains provides methods both for the debug and the release build,
 * so it's added to dagger graph in both cases.
 */
@Module
public class EtsyModule {

    @Provides
    @Singleton
    EtsySigningInterceptor providesEtsySigningInterceptor() {
        return new EtsySigningInterceptor(BuildConfig.ETSY_API_KEY);
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
    EtsyApi providesEtsyApi(@Named("EtsyRetrofit") Retrofit etsyApiAdapter) {
        return etsyApiAdapter.create(EtsyApi.class);
    }

    @Provides
    @Singleton
    public ProductRepository providesEtsyRepository(EtsyApi etsyApi, EventBus eventBus) {
        return new EtsyProductDataSource(etsyApi, eventBus);
    }
}
