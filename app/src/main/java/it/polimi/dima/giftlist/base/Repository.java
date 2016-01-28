package it.polimi.dima.giftlist.base;

import java.util.List;

import it.polimi.dima.giftlist.model.EtsyProduct;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public interface Repository {
    Observable<List<EtsyProduct>> getItems (int offset);
}
