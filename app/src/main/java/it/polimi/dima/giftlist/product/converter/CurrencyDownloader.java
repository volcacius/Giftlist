package it.polimi.dima.giftlist.product.converter;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;



import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


import it.polimi.dima.giftlist.base.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Elena on 14/02/2016.
 */
public class CurrencyDownloader {

    boolean updated;
    ConverterApi mConverterApi;
    CurrentRates.RateList mRateList;

    public CurrencyDownloader() {
        //logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //This is for adding interceptors
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();


        Retrofit retrofitAdapter = new Retrofit.Builder()
                .baseUrl(ConverterApi.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();

        mConverterApi =  retrofitAdapter.create(ConverterApi.class);

        //later rates will be saved locally, to avoid refetching all the times.
        fetchRates();
    }

    private void fetchRates() {
        Call<CurrentRates> call = mConverterApi.getRates();
        call.enqueue(new Callback<CurrentRates>() {
            @Override
            public void onResponse(Call<CurrentRates> call, Response<CurrentRates> response) {
                mRateList = response.body().getWrapper().getRateList();
                updated = true;
            }

            @Override
            public void onFailure(Call<CurrentRates> call, Throwable t) {
                System.out.println("fail " + t.getMessage());
                updated = false;
            }
        });

    }

    public CurrentRates.RateList getRateList() {
        return mRateList;
    }


    public boolean getUpdated() {
        return updated;
    }

}
