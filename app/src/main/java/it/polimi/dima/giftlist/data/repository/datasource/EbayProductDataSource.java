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

    public static String getHQImageUrl(EbayProduct product) {
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
