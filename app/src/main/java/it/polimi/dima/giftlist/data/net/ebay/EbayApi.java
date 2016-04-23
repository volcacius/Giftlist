package it.polimi.dima.giftlist.data.net.ebay;

import java.util.List;

import it.polimi.dima.giftlist.data.model.EbayProduct;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Elena on 26/03/2016.
 */
public interface EbayApi {
    String END_POINT       = "http://svcs.ebay.com/services/search/FindingService/";
    String PARAM_APP_NAME  = "SECURITY-APPNAME";
    String KEYWORD_SEARCH  = "v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD";

    @GET(KEYWORD_SEARCH+"&paginationInput.entriesPerPage=25")//manca cat id e apikey
    Observable<List<EbayProduct>> getItems(@Query("keywords") String keywords, @Query("paginationInput.pageNumber") int page);
}
//SECURITY-APPNAME=ElenaSac-Giftlist-PRD-538ccab7b-049d08b3
  //   &keywords=harry%20potter%20phoenix&paginationInput.entriesPerPage=2