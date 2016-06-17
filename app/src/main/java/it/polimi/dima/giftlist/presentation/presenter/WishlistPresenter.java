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
import timber.log.Timber;

/**
 * Created by Alessandro on 18/03/16.
 */
public class WishlistPresenter extends BaseRxLcePresenter<WishlistView, List<Product>, GetDbProductListUseCase> {

    @Inject
    public WishlistPresenter(EventBus eventBus, GetDbProductListUseCase getDbProductListUseCase, StorIOSQLite db) {
        super(eventBus, getDbProductListUseCase, db);
        Timber.d("Presenter inject");
    }

    @Override
    public void subscribe(boolean pullToRefresh) {
        Timber.d("Presenter subscribe");
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    @Override
    protected void onCompleted() {
        Timber.d("Presenter onCompleted");
        if (isViewAttached()) {
            getView().showContent();
        }
        unsubscribe();
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        Timber.d("Presenter onError");
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    protected void onNext(List<Product> data) {
        Timber.d("Presenter onNext");
        getView().setData(data);
    }


    @Subscribe
    public void onProductRemovedEvent(ProductRemovedEvent event) {
        if (isViewAttached()) {
            getView().removeProduct(event.getProduct());
        }
    }
}
