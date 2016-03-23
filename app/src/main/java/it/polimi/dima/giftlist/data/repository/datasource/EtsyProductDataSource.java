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

    EtsyApi mEtsyApi;
    EventBus mEventBus;

    @Inject
    public EtsyProductDataSource(EtsyApi etsyApi, EventBus eventBus) {
        mEtsyApi = etsyApi;
        mEventBus = eventBus;
    }

    @Override
    public Observable<List<EtsyProduct>> getProductList(String category, String keywords, int offset) {
        return mEtsyApi.getItems(category, keywords, offset);
    }
}
