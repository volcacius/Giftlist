package it.polimi.dima.giftlist.data.repository.datasource;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.net.ebay.EbayApi;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbayProductDataSource implements ProductRepository {

    EbayApi mEbayApi;
    EventBus mEventBus;
    private static final int PRODUCT_PER_PAGE = 25;
    private static final int LINE_TO_PARSE = 40;
    String QUERY_NAME_MAX = "MaxPrice";
    String QUERY_NAME_MIN = "MinPrice";


    @Inject
    public EbayProductDataSource(EbayApi ebayApi, EventBus eventBus) {
        mEbayApi = ebayApi;
        mEventBus = eventBus;
    }

    @Override
    public Observable<List<Product>> getProductList(String category, String keywords, Float maxprice, Float minprice, int offset) {
        Timber.d("ebay " + keywords + category);
        if (category!="") {
        return mEbayApi.getItems(keywords, category, offset/PRODUCT_PER_PAGE + 1,
                                QUERY_NAME_MAX, maxprice,
                                QUERY_NAME_MIN, minprice);
        } else {
            return mEbayApi.getItems(keywords, offset/PRODUCT_PER_PAGE + 1,
                    QUERY_NAME_MAX, maxprice,
                    QUERY_NAME_MIN, minprice);
        }
    }



    public static String getHQImageUrl(EbayProduct product) {
        StringBuffer myString = new StringBuffer();
        try {
            String thisLine;
            URL u = new URL(product.getProductPage());
            DataInputStream theHTML = new DataInputStream(u.openStream());
            int count = 0;
            while (count < LINE_TO_PARSE) {
                thisLine = theHTML.readLine();
                myString.append(thisLine);
                count++;
            }
        } catch (Exception e) {
            Timber.d("Ebay HQ image exception is: " + e.getMessage());
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
            Timber.d("HQ image not found for product " + product.getId());
            return product.getImageUrl();
        }
    }

    @Override
    public List<String> getProperCategory(List<String> chosenCategories) {
        return null;
    }
}
