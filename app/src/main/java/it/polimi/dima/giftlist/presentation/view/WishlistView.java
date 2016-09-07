package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;

/**
 * Created by Alessandro on 18/03/16.
 */
public interface WishlistView extends MvpLceView<List<Product>> {

    void removeProductsFromDB(List<Product> productList);
    void onProductsRemovedFromView();
    boolean isUndoBarVisible();
    void clearSearchView();
    void backlogDeletionDBCleanup();
    boolean isSelectModeEnabled();
    void initCollapsingToolbar(String name, String occasion);
}
