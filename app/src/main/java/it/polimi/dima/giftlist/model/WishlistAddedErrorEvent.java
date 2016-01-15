package it.polimi.dima.giftlist.model;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistAddedErrorEvent {

    private Wishlist wishlist;
    private Throwable exception;

    public WishlistAddedErrorEvent(it.polimi.dima.giftlist.model.Wishlist wishlist, Throwable exception) {
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
