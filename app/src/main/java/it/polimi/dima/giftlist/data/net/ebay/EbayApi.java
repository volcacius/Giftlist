package it.polimi.dima.giftlist.data.net.ebay;

import java.util.List;

import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
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
    String ADVANCED_SEARCH = "v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&descriptionSearch=true";


    @GET(ADVANCED_SEARCH+"&paginationInput.entriesPerPage=25")//manca cat id e apikey
    Observable<List<Product>> getItems(@Query("keywords") String keywords,
                                       @Query("categoryId") String category,
                                       @Query("paginationInput.pageNumber") int page,
                                       @Query("itemFilter(0).name") String queryNameMax,
                                       @Query("itemFilter(0).value") Float maxprice,
                                       @Query("itemFilter(1).name") String queryNameMin,
                                       @Query("itemFilter(1).value") Float minprice
    );

    @GET(ADVANCED_SEARCH+"&paginationInput.entriesPerPage=25")//manca cat id e apikey
    Observable<List<Product>> getItems(@Query("keywords") String keywords,
                                       @Query("paginationInput.pageNumber") int page,
                                       @Query("itemFilter(0).name") String queryNameMax,
                                       @Query("itemFilter(0).value") Float maxprice,
                                       @Query("itemFilter(1).name") String queryNameMin,
                                       @Query("itemFilter(1).value") Float minprice
    );


}
//SECURITY-APPNAME=ElenaSac-Giftlist-PRD-538ccab7b-049d08b3
  //   &keywords=harry%20potter%20phoenix&paginationInput.entriesPerPage=2