package it.polimi.dima.giftlist.presentation.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;
import rx.Observer;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.view.ProductPickerSettingsView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Elena on 22/08/2016.
 */
public class ProductPickerSettingsPresenter extends MvpBasePresenter<ProductPickerSettingsView> {

    protected StorIOSQLite db;

    @Inject
    public ProductPickerSettingsPresenter(StorIOSQLite db) {
        this.db = db;
    }

    public Wishlist getWishlist(long wishlistId) {
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

    public void addWishlist(Wishlist wishlist) {
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

        }

        @Override
        public void onNext(PutResult putResult) {

        }

    }


}