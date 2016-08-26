package it.polimi.dima.giftlist.data.repository.datasource;

import org.greenrobot.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.net.currency.CurrencyXmlModel;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.data.net.etsy.EtsyApi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
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
    public Observable<List<Product>> getProductList(String category, String keywords, Float maxprice, Float minprice, int offset) {
        return mEtsyApi.getItems(category, keywords, maxprice, minprice, offset);
    }

    @Override
    public List<String> getProperCategory(List<String> chosenCategories) {

        HashSet<String> etsyCategoriesSet = new HashSet<>();
        List<String> etsyCategories = new ArrayList<>();

        for (String c : chosenCategories) {
            switch (c) {
                case "games": {
                    etsyCategoriesSet.add("geekery");
                    break;
                }
                case "art": {
                    etsyCategoriesSet.add("art");
                    break;
                }
                case "sports": {
                    //etsyCategories.add("888");
                    break;
                }
                case "tech": {
                    break;
                }
                case "travel": {
                    etsyCategoriesSet.add("holidays");
                    break;
                }
                case "baby":
                case "newborn":
                case "christening": {
                    etsyCategoriesSet.add("baby");
                    break;
                }
                case "kid": {
                    etsyCategoriesSet.add("children");
                    etsyCategoriesSet.add("toys");
                    break;
                }
                case "young": {
                    //etsyCategories.add("172008");
                    break;
                }
                case "wedding": {
                    //ebayCategories.add("14339");
                    break;
                }
                case "handcraft": {
                    etsyCategoriesSet.add("needlecraft");
                    etsyCategoriesSet.add("knitting");
                    break;
                }
                case "anniversary":
                case "engagement": {
                    etsyCategoriesSet.add("jewelry");
                    break;
                }
            }

        }

        etsyCategories.addAll(etsyCategoriesSet);

        if (etsyCategoriesSet.isEmpty()) {
            etsyCategoriesSet.add("");
        }
        return etsyCategories;
    }
}
