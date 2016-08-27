package it.polimi.dima.giftlist.data.repository.datasource;

import org.greenrobot.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;

import clojure.lang.Compiler;
import it.polimi.dima.giftlist.data.model.CategoryType;
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
    public List<String> getProperCategory(List<CategoryType> chosenCategories) {

        HashSet<String> etsyCategoriesSet = new HashSet<>();
        List<String> etsyCategories = new ArrayList<>();

        for (CategoryType c : chosenCategories) {
            switch (c) {
                case NERD: {
                    etsyCategoriesSet.add("geekery");
                    break;
                }
                case ART: {
                    etsyCategoriesSet.add("art");
                    break;
                }
                case SPORT: {
                    //etsyCategories.add("888");
                    break;
                }
                case TECH: {
                    break;
                }
                case TRAVEL: {
                    etsyCategoriesSet.add("holidays");
                    break;
                }
                case BABY: {
                    etsyCategoriesSet.add("baby");
                    break;
                }
                case KID: {
                    etsyCategoriesSet.add("children");
                    etsyCategoriesSet.add("toys");
                    break;
                }
                case YOUNG: {
                    //etsyCategories.add("172008");
                    break;
                }
                case WEDDING: {
                    //ebayCategories.add("14339");
                    break;
                }
                case HANDCRAFT: {
                    etsyCategoriesSet.add("needlecraft");
                    etsyCategoriesSet.add("knitting");
                    break;
                }
                case ROMANTIC: {
                    etsyCategoriesSet.add("jewelry");
                    break;
                }
            }

        }

        etsyCategories.addAll(etsyCategoriesSet);

        if (etsyCategories.isEmpty()) {
            etsyCategories.add("");
        }
        return etsyCategories;
    }
}
