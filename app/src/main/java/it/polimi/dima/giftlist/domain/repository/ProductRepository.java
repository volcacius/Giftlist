package it.polimi.dima.giftlist.domain.repository;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.giftlist.data.model.CategoryType;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public interface ProductRepository {
    Observable<List<Product>> getProductList(String category, String keywords, Float maxprice, Float minprice, int offset);

    List<String> getProperCategory(List<CategoryType> chosenCategories);
}
