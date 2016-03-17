package it.polimi.dima.giftlist.data.converter;

import retrofit2.http.GET;

/**
 * Created by Elena on 14/02/2016.
 */
public interface ConverterApi {

    String BASE_URL       = "http://www.ecb.europa.eu/stats/eurofxref/";

    @GET("eurofxref-daily.xml")
    retrofit2.Call<CurrentRates> getRates();
}
