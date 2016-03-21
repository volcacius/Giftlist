package it.polimi.dima.giftlist.data.repository.datasource;

import org.greenrobot.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;

import it.polimi.dima.giftlist.data.net.currency.CurrencyXmlModel;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.EtsyProduct;

import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public class EtsyProductDataSource implements ProductRepository {

    public final static int MAX_ATTEMPS = 3;

    EtsyApi mEtsyApi;
    CurrencyDataSource mCurrencyDataSource;
    EventBus mEventBus;

    @Inject
    public EtsyProductDataSource(EtsyApi etsyApi, CurrencyDataSource currencyDataSource, EventBus eventBus) {
        mEtsyApi = etsyApi;
        mCurrencyDataSource = currencyDataSource;
        mEventBus = eventBus;
    }

    @Override
    public Observable<List<EtsyProduct>> getProductList(String category, String keywords, int offset) {
        return mEtsyApi.getItems(category, keywords, offset)
                .flatMap(etsyProductList -> Observable.from(etsyProductList))
                //.doOnNext(etsyProduct -> etsyProduct.setConvertedPrice(convert(etsyProduct.getCurrency(), etsyProduct.getPrice())))
                .toList();
    }

    /*
    private float convert(String currency, Float price) {
        return round(price / mRateList.getConversionRate(currency), 2);
    }

    public float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
    */
}
