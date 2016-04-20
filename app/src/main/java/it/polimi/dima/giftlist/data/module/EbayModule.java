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
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.net.ebay.EbayApi;
import it.polimi.dima.giftlist.data.net.ebay.EbayResultsDeserializer;
import it.polimi.dima.giftlist.data.net.ebay.EbaySigningInterceptor;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import retrofit2.Retrofit;

/**
 * Created by Elena on 26/03/2016.
 */
@Module
public class EbayModule {

    @Provides
    @Singleton
    EbaySigningInterceptor providesEbaySigningInterceptor() {
        return new EbaySigningInterceptor(BuildConfig.EBAY_APP_NAME);
    }

    @Provides
    @Singleton
    EbayResultsDeserializer providesEbayResultsDeserializer() {
        return new EbayResultsDeserializer();
    }

    @Provides
    @Singleton
    @Named("EbayGson")
    Gson providesEbayGsonInstance(EbayResultsDeserializer ebayResultsDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<EbayProduct>>() {}.getType(), ebayResultsDeserializer)
                .create();
    }

    @Provides
    @Singleton
    EbayApi providesEbayApi(@Named("EbayRetrofit") Retrofit ebayApiAdapter) {
        return ebayApiAdapter.create(EbayApi.class);
    }

    @Provides
    @Singleton
    @Named("EbayRepository")
    public ProductRepository<Product> providesEbayRepository(EbayApi ebayApi, EventBus eventBus) {
        return new EbayProductDataSource(ebayApi, eventBus);
    }
}

