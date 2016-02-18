package it.polimi.dima.giftlist.product.converter;

import java.util.List;

import okhttp3.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Elena on 14/02/2016.
 */
public interface ConverterApi {

    String BASE_URL       = "http://www.ecb.europa.eu/stats/eurofxref/";

    @GET("eurofxref-daily.xml")
    retrofit2.Call<CurrentRates> getRates();
}
