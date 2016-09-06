package it.polimi.dima.giftlist.presentation.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.GetWishlistUseCase;
import it.polimi.dima.giftlist.presentation.view.WishlistSettingsView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Elena on 10/08/2016.
 */
public class WishlistSettingsPresenter extends BaseRxLcePresenter<WishlistSettingsView, Wishlist, GetWishlistUseCase> {

    @Inject
    public WishlistSettingsPresenter(GetWishlistUseCase getWishlistUseCase, StorIOSQLite db) {
        super(getWishlistUseCase, db);
    }

    @Override
    public void subscribe(boolean pullToRefresh) {
        if(!useCase.isUnsubscribed()) {
            unsubscribe();
        }
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    @Override
    protected void onCompleted() {
        //DB subscriptions does not complete
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    protected void onNext(Wishlist data) {
        if (isViewAttached()) {
            getView().showLoading(false);
        }
        getView().setData(data);
        if (isViewAttached()) {
            getView().showContent();
        }
    }

    public void addWishlist(long wishlistId, String wishlistName, String occasion, int displayOrder) {
        Timber.d("Wishlist insert query is: %s", WishlistTable.getCustomPutQuery(wishlistId, wishlistName, occasion, displayOrder));
        db.executeSQL()
                .withQuery(RawQuery.builder()
                        .query(WishlistTable.getCustomPutQuery(wishlistId, wishlistName, occasion, displayOrder))
                        .affectsTables(WishlistTable.TABLE)
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(new SingleSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        Timber.d("Success in inserting the wishlist %d", wishlistId);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in inserting the wishlist %d", wishlistId);

                    }
                });
    }

    public void updateWishlist(long wishlistId, String wishlistName, String occasion) {
        Timber.d("Wishlist update query is: %s", WishlistTable.getNameOccasionUpdateQuery(wishlistId, wishlistName, occasion));
        db.executeSQL()
                .withQuery(RawQuery.builder()
                        .query(WishlistTable.getNameOccasionUpdateQuery(wishlistId, wishlistName, occasion))
                        .affectsTables(WishlistTable.TABLE)
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(new SingleSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        Timber.d("Success in updating the wishlist %d", wishlistId);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in updating the wishlist %d", wishlistId);

                    }
                });
    }
}
