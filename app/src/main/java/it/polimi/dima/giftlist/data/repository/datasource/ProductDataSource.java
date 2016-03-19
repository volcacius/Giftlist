package it.polimi.dima.giftlist.data.repository.datasource;

import java.util.List;

import it.polimi.dima.giftlist.data.model.Product;
import rx.Observable;

/**
 * Created by Alessandro on 18/03/16.
 */
public interface ProductDataSource {

    Observable<List<Product>> getProductList();

    Observable<Product> getProduct(final int productId);
}
