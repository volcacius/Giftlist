package it.polimi.dima.giftlist.data.module;

import org.greenrobot.eventbus.EventBus;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.net.currency.CurrencyApi;
import it.polimi.dima.giftlist.data.net.currency.CurrencyConverter;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;
import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.util.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import timber.log.Timber;

/**
 * Created by Alessandro on 21/03/16.
 * EtsyModule contains provides methods both for the debug and the release build,
 * so it's added to dagger graph in both cases.
 */
@Module
public class CurrencyModule {

    @Provides
    @Singleton
    @Named("CurrencyXml")
    SimpleXmlConverterFactory providesSimpleCurrencyXmlConverterFactory(@Named("CurrencyXmlSerializer") Serializer serializer) {
        return SimpleXmlConverterFactory.create(serializer);
    }

    @Provides
    @Singleton
    @Named("CurrencyXmlSerializer")
    //SimpleXml requires me to manually bind the Currency entity with its converter
    Serializer providesCurrencySerializer() {
        Registry registry = new Registry();
        try {
            registry.bind(Currency.class, CurrencyConverter.class);
        } catch (Exception e) {
            Timber.e("Exception occured while binding currency xml converter: %s", e.getMessage());
        }
        Strategy strategy = new RegistryStrategy(registry);
        return new Persister(strategy);
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
    public CurrencyRepository providesCurrencyRepository(CurrencyApi currencyApi, EventBus eventBus) {
        return new CurrencyDataSource(currencyApi, eventBus);
    }
}
