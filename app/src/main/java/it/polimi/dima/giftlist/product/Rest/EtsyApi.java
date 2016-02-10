package it.polimi.dima.giftlist.product.Rest;

import java.util.List;

import it.polimi.dima.giftlist.model.EtsyProduct;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public interface EtsyApi {
    String END_POINT       = "https://openapi.etsy.com/";
    String PARAM_API_KEY   = "api_key";

    @GET("v2/listings/active?fields=listing_id,title,price,description&includes=Images(url_570xN,hex_code)")
    Observable<List<EtsyProduct>> getItems(@Query("category") String category, @Query("offset") int offset);

}
