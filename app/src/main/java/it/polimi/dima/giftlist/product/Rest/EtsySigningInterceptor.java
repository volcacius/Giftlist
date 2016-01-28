package it.polimi.dima.giftlist.product.Rest;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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

        HttpUrl.Builder authorizedUrlBuilder = oldRequest.httpUrl().newBuilder()
                .scheme(oldRequest.httpUrl().scheme())
                .host(oldRequest.httpUrl().host());

        authorizedUrlBuilder.addQueryParameter(EtsyApi.PARAM_API_KEY, mApiKey);

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}

