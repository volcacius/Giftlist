package it.polimi.dima.giftlist.presentation.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.GetWishlistUseCase;
import it.polimi.dima.giftlist.presentation.event.ProductAddedEvent;
import it.polimi.dima.giftlist.presentation.event.ProductRemovedEvent;
import it.polimi.dima.giftlist.presentation.view.WishlistView;

/**
 * Created by Alessandro on 18/03/16.
 */
public class WishlistPresenter extends BaseRxLcePresenter<WishlistView, Wishlist, GetWishlistUseCase> {

    @Inject
    public WishlistPresenter(EventBus eventBus, GetWishlistUseCase getWishlistUseCase) {
        super(eventBus, getWishlistUseCase);
    }

    @Override
    public void subscribe(boolean pullToRefresh) {

    }

    @Override
    protected void onCompleted() {

    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    protected void onNext(Wishlist data) {

    }

    @Subscribe
    public void onProductAddedEvent(ProductAddedEvent event) {
        if (isViewAttached()) {
            getView().addProduct(event.getProduct());
        }
    }

    @Subscribe
    public void onProductRemovedEvent(ProductRemovedEvent event) {
        if (isViewAttached()) {
            getView().removeProduct(event.getProduct());
        }
    }
}
