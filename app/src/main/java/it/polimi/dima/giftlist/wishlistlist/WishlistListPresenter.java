package it.polimi.dima.giftlist.wishlistlist;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import it.polimi.dima.giftlist.DummyInterface;
import it.polimi.dima.giftlist.base.BaseRxLcePresenter;
import it.polimi.dima.giftlist.model.Person;
import it.polimi.dima.giftlist.model.Wishlist;
import it.polimi.dima.giftlist.model.WishlistAddedEvent;
import it.polimi.dima.giftlist.model.WishlistListProvider;
import it.polimi.dima.giftlist.model.WishlistRemovedEvent;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListPresenter extends BaseRxLcePresenter<WishlistListView, List<Wishlist>> {

    protected EventBus eventBus;
    protected WishlistListProvider wishlistListProvider;
    protected DummyInterface mDummyAPI;

    //@Inject
    public WishlistListPresenter(WishlistListProvider wishlistListProvider, EventBus eventBus, DummyInterface dummyAPI) {
        this.eventBus = eventBus;
        this.wishlistListProvider = wishlistListProvider;
        this.mDummyAPI = dummyAPI;
    }

    @Inject public WishlistListPresenter(DummyInterface dummyAPI) {
        this.mDummyAPI = dummyAPI;
    }

    @Override
    public void attachView(WishlistListView view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        eventBus.unregister(this);
    }

    public void load(boolean pullToRefresh, Person person) {
        subscribe(wishlistListProvider.getWishlistsOfPerson(person.getName()), pullToRefresh);
    }

    public void loadWishlistList(boolean pullToRefresh) {

        Observable<List<Wishlist>> observable =
                Observable.from(mDummyAPI.getDummyList()).flatMap(new Func1<List<Wishlist>, Observable<List<Wishlist>>>() {
                    @Override public Observable<List<Wishlist>> call(List<Wishlist> repos) {
                        Collections.shuffle(repos);
                        return Observable.just(repos);
                    }
                });

        subscribe(observable, pullToRefresh);
    }

    public void onEventMainThread(WishlistAddedEvent event) {
        //TODO
    }

    public void onEventMainThread(WishlistRemovedEvent event) {
        //TODO
    }

}
