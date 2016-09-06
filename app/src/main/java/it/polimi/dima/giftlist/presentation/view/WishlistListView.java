package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import it.polimi.dima.giftlist.data.model.Wishlist;

/**
 * Created by Alessandro on 08/01/16.
 */
public interface WishlistListView extends MvpLceView<List<Wishlist>> {

    void removeWishlistsFromDBByIds(List<Long> wishlistIds);
    void onWishlistsRemovedFromView();
    boolean isUndoBarVisible();
    void clearSearchView();
    void backlogDeletionDBCleanup();
    boolean isSelectModeEnabled();
}
