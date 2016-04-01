package it.polimi.dima.giftlist.domain.interactor;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.presentation.event.ImageUrlRetrievedEvent;
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
    protected EventBus eventBus;

    @Inject
    public GetProductListUseCase(ProductRepository productRepository,
                                 CurrencyRepository currencyRepository,
                                 String category,
                                 String keywords,
                                 EventBus eventBus) {
        this.currencyRepository = currencyRepository;
        this.productRepository = productRepository;
        this.category = category;
        this.keywords = keywords;
        this.searchOffset = STARTING_OFFSET;

        //TODO notify adapter to reload image
        this.eventBus = eventBus;
    }

    @Override
    protected Observable<List<Product>> buildUseCaseObservable() {
        Observable<List<Product>> productList = productRepository.getProductList(category, keywords, searchOffset*PRODUCT_PER_PAGE);
        Observable<List<Currency>> currencyList = currencyRepository.getCurrencyList();
        searchOffset++;
        return Observable.combineLatest(productList, currencyList, new Func2<List<Product>, List<Currency>, List<Product>>() {
            @Override
            public List<Product> call(List<Product> productList, List<Currency> currencies) {

                if(productList.get(0).getClass().equals(EbayProduct.class)){
                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run(){
                            for (Product p : productList) {
                                p.setImageUrl(getHQImageUrl((EbayProduct) p));
                            }
                            eventBus.post(new ImageUrlRetrievedEvent());

                        }
                    });
                    thread.start();
                }

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

    private String getHQImageUrl(EbayProduct product) {

        StringBuffer myString = new StringBuffer();
        try {
            String thisLine;
            URL u = new URL(product.getProductPage());
            DataInputStream theHTML = new DataInputStream(u.openStream());
            int count = 0;
            while (count < 20) {
                thisLine = theHTML.readLine();
                myString.append(thisLine);
                count++;

            }

        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }

        //String to match:
        //<meta  property="og:image" content="http://i.ebayimg.com/images/i/322010611314-0-1/s-l1000.jpg" />

        String pattern = "(<meta  property=\"og:image\" content=\"([^\"]*)\" />)";

        Pattern pat = Pattern.compile(pattern);
        Matcher m = pat.matcher(myString);
        if (m.find()) {
            return m.group(2);
        }
        else {
            return product.getImageUrl();
        }

    }
}
