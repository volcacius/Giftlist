package it.polimi.dima.giftlist.presentation.event;

import it.polimi.dima.giftlist.data.model.Wishlist;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistAddedErrorEvent {

    private Wishlist wishlist;
    private Throwable exception;

    public WishlistAddedErrorEvent(Wishlist wishlist, Throwable exception) {
        this.wishlist = wishlist;
        this.exception = exception;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public Throwable getException() {
        return exception;
    }
}
