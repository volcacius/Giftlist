package it.polimi.dima.giftlist.data.net.currency;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Elena on 14/02/2016.
 */
public interface CurrencyApi {

    String BASE_URL = "http://www.ecb.europa.eu/stats/eurofxref/";

    @GET("eurofxref-daily.xml")
    Observable<CurrencyXmlModel> getCurrencies();
}
