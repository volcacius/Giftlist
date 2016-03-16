package it.polimi.dima.giftlist.data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import it.polimi.dima.giftlist.presentation.model.Wishlist;
import it.polimi.dima.giftlist.presentation.exception.WishlistNotFoundException;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListProvider {

    private List<Wishlist> wishlistList;
    private AtomicInteger lastId;

    public WishlistListProvider(WishlistGenerator generator) {
        wishlistList = generator.generateWishlists();
        lastId = new AtomicInteger(wishlistList.get(wishlistList.size() - 1).getId());
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
            @Override public Observable<Wishlist> call(List<Wishlist> wishlistList) {

                if (wishlistList.isEmpty()) {
                    return Observable.error(new WishlistNotFoundException());
                }

                return Observable.just(wishlistList.get(0));
            }
        });
    }

    private Observable<List<Wishlist>> getFilteredWishlistList(Func1<Wishlist, Boolean> func1) {
        return null;
    }

}
