package it.polimi.dima.giftlist.model;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistAddedEvent {

    private Wishlist wishlist;

    public WishlistAddedEvent(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }
}
