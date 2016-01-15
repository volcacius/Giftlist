package it.polimi.dima.giftlist.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistsProvider {

    private List<Wishlist> wishlists;
    private AtomicInteger lastId;

    public WishlistsProvider(WishlistGenerator generator) {
        wishlists = generator.generateWishlists();
        lastId = new AtomicInteger(wishlists.get(wishlists.size() - 1).getId());
    }

    public Observable<List<Wishlist>> getWishlistsOfPerson(String name) {
        return null;
    }

    public Observable<Wishlist> getWishlist(final int id) {
        return getFilteredWishlistList(new Func1<Wishlist, Boolean>() {
            @Override
            public Boolean call(Wishlist wishlist) {
                return wishlist.getId() == id;
            }
        }).flatMap(new Func1<List<Wishlist>, Observable<Wishlist>>() {
            @Override public Observable<Wishlist> call(List<Wishlist> wishlists) {

                if (wishlist == null || wishlists.isEmpty()) {
                    return Observable.error(new WishlistNotFoundException());
                }

                return Observable.just(mails.get(0));
            }
        });
    }

    private Observable<List<Wishlist>> getFilteredWishlistList(Func1<Wishlist, Boolean> func1) {
        return null;
    }

}
