package it.polimi.dima.giftlist.data.repository.datasource;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.net.ebay.EbayApi;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import rx.Observable;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbayProductDataSource implements ProductRepository {

    EbayApi mEbayApi;
    EventBus mEventBus;
    private static final int PRODUCT_PER_PAGE = 25;

    @Inject
    public EbayProductDataSource(EbayApi ebayApi, EventBus eventBus) {
        mEbayApi = ebayApi;
        mEventBus = eventBus;
    }

    @Override
    public Observable<List<EbayProduct>> getProductList(String category, String keywords, int offset) {
        return mEbayApi.getItems(keywords, offset/PRODUCT_PER_PAGE + 1);
    }
}
