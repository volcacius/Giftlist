package it.polimi.dima.giftlist.domain.interactor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import rx.Observable;
import rx.functions.Func2;

/**
 * Created by Elena on 27/01/2016.
 */
public class GetProductListUseCase extends UseCase<List<Product>> {

    private static final int PRODUCT_PER_PAGE = 25;
    private static final int DIGITS = 2;
    private static final int STARTING_OFFSET = 0;
    private ProductRepository productRepository;
    private CurrencyRepository currencyRepository;
    private String category;
    private String keywords;
    private int searchOffset;

    @Inject
    public GetProductListUseCase(ProductRepository productRepository,
                                 CurrencyRepository currencyRepository,
                                 String category,
                                 String keywords) {
        this.currencyRepository = currencyRepository;
        this.productRepository = productRepository;
        this.category = category;
        this.keywords = keywords;
        this.searchOffset = STARTING_OFFSET;
    }

    @Override
    protected Observable<List<Product>> buildUseCaseObservable() {
        Observable<List<Product>> productList = productRepository.getProductList(category, keywords, searchOffset*PRODUCT_PER_PAGE);
        Observable<List<Currency>> currencyList = currencyRepository.getCurrencyList();
        searchOffset++;
        return Observable.combineLatest(productList, currencyList, new Func2<List<Product>, List<Currency>, List<Product>>() {
            @Override
            public List<Product> call(List<Product> productList, List<Currency> currencies) {
                for (Product p : productList) {
                    for (Currency c : currencies) {
                        if (c.getCurrencyType().equals(p.getCurrencyType())) {
                            p.setConvertedPrice(round(p.getPrice() / c.getRate(), DIGITS));
                        }
                    }
                }
                return productList;
            }
        });
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
