package it.polimi.dima.giftlist.presentation.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.domain.interactor.GetDbProductListUseCase;
import it.polimi.dima.giftlist.presentation.event.ProductRemovedEvent;
import it.polimi.dima.giftlist.presentation.view.WishlistView;

/**
 * Created by Alessandro on 18/03/16.
 */
public class WishlistPresenter extends BaseRxLcePresenter<WishlistView, List<Product>, GetDbProductListUseCase> {

    @Inject
    public WishlistPresenter(EventBus eventBus, GetDbProductListUseCase getDbProductListUseCase, StorIOSQLite db) {
        super(eventBus, getDbProductListUseCase, db);
    }

    @Override
    public void subscribe(boolean pullToRefresh) {
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    @Override
    protected void onCompleted() {
        unsubscribe();
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    protected void onNext(List<Product> data) {
        getView().setData(data);
    }


    @Subscribe
    public void onProductRemovedEvent(ProductRemovedEvent event) {
        if (isViewAttached()) {
            getView().removeProduct(event.getProduct());
        }
    }
}
