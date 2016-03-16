package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import it.polimi.dima.giftlist.presentation.model.Wishlist;

/**
 * Created by Alessandro on 08/01/16.
 */
public interface WishlistListView extends MvpLceView<List<Wishlist>> {

    public void addWishlist(int wishlistId);

    public void removeWishlist(int wishlistId);

    public void showAddingFailed(Wishlist wishlist);

    public void showRemovingFailed(Wishlist wishlist);

}
