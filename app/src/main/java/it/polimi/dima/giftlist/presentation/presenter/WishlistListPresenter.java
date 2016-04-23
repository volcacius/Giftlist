package it.polimi.dima.giftlist.presentation.presenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.GetWishlistListUseCase;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListPresenter extends BaseRxLcePresenter<WishlistListView, List<Wishlist>, GetWishlistListUseCase> {

    @Inject
    public WishlistListPresenter(EventBus eventBus, GetWishlistListUseCase useCase) {
        super(eventBus, useCase);
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
    protected void onNext(List<Wishlist> data) {

    }
}
