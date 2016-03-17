package it.polimi.dima.giftlist.data.repository;

import java.util.List;

import it.polimi.dima.giftlist.data.model.EtsyProduct;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public interface ItemRepository {
    Observable<List<EtsyProduct>> getItems (String category, String keywords, int offset);
}
