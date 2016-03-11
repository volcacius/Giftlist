package it.polimi.dima.giftlist.product.converter;

/**
 * Created by Elena on 22/02/2016.
 */
public class RetrofitEvent{
    boolean isRetrofitCompleted;

    public RetrofitEvent(boolean success){
        isRetrofitCompleted = success;
    }
}
