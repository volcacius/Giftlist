package it.polimi.dima.giftlist.domain.interactor;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import rx.Observable;
import rx.functions.Func2;
import timber.log.Timber;

/**
 * Created by Elena on 27/01/2016.
 */
public class GetNetProductsUseCase extends UseCase<List<Product>> {

    private static final int PRODUCT_PER_PAGE = 25;
    private static final int DIGITS = 2;
    private static final int STARTING_OFFSET = 0;
    private List<ProductRepository> productRepositoryList;
    private CurrencyRepository currencyRepository;
    private String category;
    private String keywords;
    private Float maxprice;
    private Float minprice;
    private int searchOffset;
    protected EventBus eventBus;
    private long wishlistId;

    @Inject
    public GetNetProductsUseCase(List<ProductRepository> productRepositoryList,
                                 CurrencyRepository currencyRepository,
                                 String category,
                                 String keywords,
                                 Float maxprice,
                                 Float minprice,
                                 long wishlistId,
                                 EventBus eventBus) {
        this.currencyRepository = currencyRepository;
        this.productRepositoryList = productRepositoryList;
        this.category = category;
        this.keywords = keywords;
        this.maxprice = maxprice;
        this.minprice = minprice;
        this.searchOffset = STARTING_OFFSET;
        this.wishlistId = wishlistId;
        this.eventBus = eventBus;
    }

    @RxLogObservable
    @Override
    //At the end of the chain I need to wrap the product as a single valued list, since list<product> is the type accepted as model accross the whole use case
    protected Observable<List<Product>> buildUseCaseObservable() {
        List<Observable<List<Product>>> productListObservableList = new ArrayList<>();
        for (ProductRepository pr : productRepositoryList) {
            productListObservableList.add(pr.getProductList(category, keywords, maxprice, minprice, searchOffset*PRODUCT_PER_PAGE));
        }
        Observable<List<Currency>> currencyList = currencyRepository.getCurrencyList();
        searchOffset++;
        return Observable.merge(productListObservableList)
                .flatMap(products -> Observable.from(products))
                .withLatestFrom(currencyList, new Func2<Product, List<Currency>, Product>() {
                    @Override
                    public Product call(Product product, List<Currency> currencies) {
                        //Retrieve and set HQ ebay image
                        Timber.d("Product is of type " + product.getClass().getSimpleName());
                        if (product.getClass().equals(EbayProduct.class)) {
                            product.setImageUrl(EbayProductDataSource.getHQImageUrl((EbayProduct) product));
                        }
                        //Set converted price
                        for (Currency c : currencies) {
                            if (c.getCurrencyType().equals(product.getCurrencyType())) {
                                product.setConvertedPrice(round(product.getPrice() / c.getRate(), DIGITS));
                            }
                        }
                        //Set wishlist id
                        product.setWishlistId(wishlistId);
                        return product;
                    }
        }).map(product -> new ArrayList<Product>(Arrays.asList(product)));
    }

    private float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
