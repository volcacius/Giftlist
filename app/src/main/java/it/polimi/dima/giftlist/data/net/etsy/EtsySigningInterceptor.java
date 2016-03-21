package it.polimi.dima.giftlist.data.net.etsy;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Elena on 27/01/2016.
 */
public class EtsySigningInterceptor implements Interceptor {

    private final String mApiKey;

    public EtsySigningInterceptor(String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();

        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());

        authorizedUrlBuilder.addQueryParameter(EtsyApi.PARAM_API_KEY, mApiKey);

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}

