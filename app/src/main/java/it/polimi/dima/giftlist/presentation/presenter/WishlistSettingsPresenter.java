package it.polimi.dima.giftlist.presentation.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetObject;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.event.WishlistAddedEvent;
import it.polimi.dima.giftlist.presentation.view.WishlistSettingsView;
import rx.Observer;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Elena on 10/08/2016.
 */
public class WishlistSettingsPresenter extends MvpBasePresenter<WishlistSettingsView> {

    protected StorIOSQLite db;

    @Inject
    public WishlistSettingsPresenter(StorIOSQLite db) {
        this.db = db;
    }

    public Wishlist onWishlistSettingsLoaded(long wishlistId) {
        return db.get()
                .object(Wishlist.class)
                .withQuery(Query.builder()
                        .table(WishlistTable.TABLE)
                        .where("id = ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    public void onWishlistAdded(Wishlist wishlist) {
        Observer observer = new WishlistPutObserver();
        db.put()
                .object(wishlist)
                .prepare()
                .asRxObservable()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(observer);
    }

    private class WishlistPutObserver implements Observer<PutResult> {
        @Override
        public void onCompleted() {
        }
        @Override
        public void onError(Throwable e) {
            getView().showWishlistAddedError();
        }

        @Override
        public void onNext(PutResult putResult) {
            getView().showWishlistAddedSuccess();
        }

    }


}
