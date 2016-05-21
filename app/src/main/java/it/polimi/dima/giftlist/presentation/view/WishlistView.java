package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;

/**
 * Created by Alessandro on 18/03/16.
 */
public interface WishlistView extends MvpLceView<List<Product>> {

    public void removeProduct(Product product);

}
