package it.polimi.dima.giftlist.base;

import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public interface UseCase <T> {

    Observable<T> execute();
}
