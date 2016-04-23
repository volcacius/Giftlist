package it.polimi.dima.giftlist.data.net.ebay;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbaySigningInterceptor implements Interceptor {

    private final String mAppName;

    public EbaySigningInterceptor(String appName) {
        mAppName = appName;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());

        authorizedUrlBuilder.addQueryParameter(EbayApi.PARAM_APP_NAME, mAppName);

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}
