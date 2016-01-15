package it.polimi.dima.giftlist.wishlistlist;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import it.polimi.dima.giftlist.base.BaseRxLcePresenter;
import it.polimi.dima.giftlist.model.Person;
import it.polimi.dima.giftlist.model.Wishlist;
import it.polimi.dima.giftlist.model.WishlistAddedEvent;
import it.polimi.dima.giftlist.model.WishlistListProvider;
import it.polimi.dima.giftlist.model.WishlistRemovedEvent;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListPresenter extends BaseRxLcePresenter<WishlistListView, List<Wishlist>> {

    protected EventBus eventBus;
    protected WishlistListProvider wishlistListProvider;

    @Inject
    public WishlistListPresenter(WishlistListProvider wishlistListProvider, EventBus eventBus) {
        this.eventBus = eventBus;
        this.wishlistListProvider = wishlistListProvider;
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

    public void onEventMainThread(WishlistAddedEvent event) {
        //TODO
    }

    public void onEventMainThread(WishlistRemovedEvent event) {
        //TODO
    }

}
