package it.polimi.dima.giftlist.product.Rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.BuildConfig;
import it.polimi.dima.giftlist.base.HttpLoggingInterceptor;
import it.polimi.dima.giftlist.base.Repository;
import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.model.exceptions.rest.NetworkErrorException;
import it.polimi.dima.giftlist.model.exceptions.rest.ServerErrorException;
import it.polimi.dima.giftlist.model.exceptions.rest.UnknownErrorException;
import it.polimi.dima.giftlist.util.HttpErrors;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public class EtsyRestDataSource implements Repository {
    private final EtsyApi mEtsyApi;
    //private final GithubService mGithubService;
    public final static int MAX_ATTEMPS = 3;

    @Inject
    public EtsyRestDataSource() {
        //This is for adding interceptors
        OkHttpClient client = new OkHttpClient();

        //logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //authentication interceptor: appends the api_key
        EtsySigningInterceptor signingIterceptor = new EtsySigningInterceptor(BuildConfig.ETSY_API_KEY);

        client.interceptors().add(signingIterceptor);
        client.interceptors().add(logging);

        Gson customGsonInstance = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<EtsyProduct>>() {}.getType(),
                        new EtsyResultsDeserializer())
                .create();

        Retrofit etsyApiAdapter = new Retrofit.Builder()
                .baseUrl(EtsyApi.END_POINT)
                .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        mEtsyApi =  etsyApiAdapter.create(EtsyApi.class);

    }



    @Override
    public Observable<List<EtsyProduct>> getItems(String category, int offset) {
        return mEtsyApi.getItems(category, offset);/*
                .onErrorResumeNext(throwable -> {
                    String errorMessage = throwable.getMessage();
                    switch (errorMessage){
                        case HttpErrors.SERVER_ERROR: return Observable.error(new ServerErrorException());
                     //   case HttpErrors.TIMEOUT: return Observable.error(new NetworkErrorException());
                        default: return Observable.error(new UnknownErrorException());
                    }
                });*/

                /*.flatMap(etsyProductList -> Observable.from(etsyProductList))
                .doOnNext(etsyProduct -> etsyProduct.setImageUrl(null))
                .toList();*/
    }

}
