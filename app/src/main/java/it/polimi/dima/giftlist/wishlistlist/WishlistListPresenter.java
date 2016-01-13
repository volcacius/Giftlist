package it.polimi.dima.giftlist.wishlistlist;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.DummyInterface;
import it.polimi.dima.giftlist.DummyList;
import it.polimi.dima.giftlist.base.MvpLceRxPresenter;
import it.polimi.dima.giftlist.model.Wishlist;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListPresenter extends MvpLceRxPresenter<WishlistListView, List<Wishlist>> {


    DummyInterface mDummyAPI;

    @Inject public WishlistListPresenter(DummyInterface dummyAPI) {
        this.mDummyAPI = dummyAPI;
    }

    public void loadRepos(boolean pullToRefresh) {

        Observable<List<Wishlist>> observable =
                Observable.from(mDummyAPI.getDummyList()).flatMap(new Func1<List<Wishlist>, Observable<List<Wishlist>>>() {
                    @Override public Observable<List<Wishlist>> call(List<Wishlist> repos) {
                        Collections.shuffle(repos);
                        return Observable.just(repos);
                    }
                });

        subscribe(observable, pullToRefresh);
    }

}
