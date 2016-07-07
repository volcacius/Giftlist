package it.polimi.dima.giftlist.data.net.etsy;

import java.util.List;

import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public interface EtsyApi {
    String END_POINT       = "https://openapi.etsy.com/";
    String PARAM_API_KEY   = "api_key";

    @GET("v2/listings/active?fields=listing_id,title,currency_code,price,description&includes=Images(url_570xN,hex_code)")
    Observable<List<EtsyProduct>> getItems(@Query("category") String category, @Query("offset") int offset);

    @GET("v2/listings/active?fields=listing_id,title,currency_code,price,description&includes=Images(url_570xN,hex_code)")
    Observable<List<Product>> getItems(@Query("category") String category,
                                       @Query("tags") String keywords,
                                       @Query("max_price") Float maxprice,
                                       @Query("min_price") Float minprice,
                                       @Query("offset") int offset);
    ///@Query("max_price") Float maxprice, NB: needs to be converted in USD

}
