package it.polimi.dima.giftlist.presentation.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.GetWishlistListUseCase;
import it.polimi.dima.giftlist.presentation.event.WishlistAddedEvent;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListPresenter extends BaseRxLcePresenter<WishlistListView, List<Wishlist>, GetWishlistListUseCase> {

    @Inject
    public WishlistListPresenter(EventBus eventBus, GetWishlistListUseCase useCase, StorIOSQLite db) {
        super(eventBus, useCase, db);
    }

    @Override
    public void subscribe(boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }
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
    protected void onNext(List<Wishlist> data) {
        if (isViewAttached()) {
            getView().showContent();
        }
        getView().setData(data);
    }

    @Subscribe
    public void onWishlistAddedEvent(WishlistAddedEvent event) {
        Wishlist wishlist = event.getWishlist();
        Observer observer = new WishlistPutObserver();
        db.put()
                .object(wishlist)
                .prepare()
                .asRxObservable()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(observer);
    }

    private class WishlistPutObserver implements Observer<PutResults<Wishlist>> {
        @Override
        public void onCompleted() {
        }
        @Override
        public void onError(Throwable e) {
            getView().showWishlistAddedError();
        }
        @Override
        public void onNext(PutResults<Wishlist> wishlistPutResults) {
            getView().showWishlistAddedSuccess();
        }
    }

}
