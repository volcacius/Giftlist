package it.polimi.dima.giftlist.presentation.presenter;

import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.db.resolver.delete.WishlistDeleteResolver;
import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.GetWishlistListUseCase;
import it.polimi.dima.giftlist.presentation.event.WishlistAddedEvent;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import rx.Observer;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListPresenter extends BaseRxLcePresenter<WishlistListView, List<Wishlist>, GetWishlistListUseCase> {

    @Inject
    public WishlistListPresenter(GetWishlistListUseCase useCase, StorIOSQLite db) {
        super(useCase, db);
    }

    @Override
    @DebugLog
    public void subscribe(boolean pullToRefresh) {
        if(!useCase.isUnsubscribed()) {
            unsubscribe();
        }
        useCase.execute(new BaseSubscriber(pullToRefresh));
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

    }

    @Override
    protected void onCompleted() {
        //DB subscriptions do not complete
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    @DebugLog
    protected void onNext(List<Wishlist> data) {
        getView().showLoading(false);
        List<Wishlist> orderedList = new LinkedList<>(data);
        Collections.sort(orderedList);
        getView().setData(orderedList);
        if (isViewAttached()) {
            getView().showContent();
        }
    }

    //I do not want the observer to emit an unnecessary onNext
    //So I manually run an update query without adding the affected table
    public void updateWishlistListOrder(List<Wishlist> wishlistList) {
        for (Wishlist w : wishlistList) {
            db.executeSQL()
                    .withQuery(RawQuery.builder()
                            .query(WishlistTable.getDisplayOrderUpdateQuery(w.getId(), w.getDisplayOrder()))
                            .build())
                    .prepare()
                    .asRxSingle()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<Object>() {
                        @Override
                        public void onSuccess(Object value) {
                            Timber.d("Wishlist %d is set at order %d in DB", w.getId(), w.getDisplayOrder());
                        }

                        @Override
                        public void onError(Throwable error) {
                            Timber.d("Error in setting wishlist %d at order %d in DB", w.getId(), w.getDisplayOrder());
                        }
                    });
        }
    }

    public void removeWishlist(long wishlistId) {
        Timber.d("deleting wishlist %d", wishlistId);
        deleteImages(wishlistId);
        //I do not want the observer to emit an onNext since it would mess up the deletion
        //So I manually run a delete query without adding the affected table
        db.executeSQL()
                .withQuery(RawQuery.builder()
                        .query(WishlistTable.getCustomDeleteQuery(wishlistId))
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(new SingleSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        Timber.d("Success in deleting the wishlist %d", wishlistId);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in deleting the wishlist %d", wishlistId);
                    }
                });

        db.delete()
                .byQuery(DeleteQuery.builder()
                        .table(EtsyProductTable.TABLE)
                        .where(EtsyProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(new SingleSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        Timber.d("Success in deleting the wishlist %d etsy products", wishlistId);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in deleting the wishlist %d etsy products", wishlistId);
                    }
                });

        db.delete()
                .byQuery(DeleteQuery.builder()
                        .table(EbayProductTable.TABLE)
                        .where(EbayProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                .subscribe(new SingleSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object value) {
                        Timber.d("Success in deleting the wishlist %d ebay products", wishlistId);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in deleting the wishlist %d ebay products", wishlistId);
                    }
                });
    }

    private void deleteImages(long wishlistId) {
        db.get()
                .listOfObjects(EtsyProduct.class)
                .withQuery(Query.builder()
                        .table(EtsyProductTable.TABLE)
                        .where(EtsyProductTable.COLUMN_WISHLIST_ID + " = ?")
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<EtsyProduct>>() {
                    @Override
                    public void onSuccess(List<EtsyProduct> value) {
                        for (EtsyProduct p : value) {
                            File fdelete = new File(p.getImageUri());
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    Timber.d("file Deleted: %s", p.getImageUri());
                                } else {
                                    Timber.d("file not Deleted: %s", p.getImageUri());
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in deleting etsy images");
                    }
                });

        db.get()
                .listOfObjects(EbayProduct.class)
                .withQuery(Query.builder()
                        .table(EbayProductTable.TABLE)
                        .where(EbayProductTable.COLUMN_WISHLIST_ID + " = ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<EbayProduct>>() {
                    @Override
                    public void onSuccess(List<EbayProduct> value) {
                        for (EbayProduct p : value) {
                            File fdelete = new File(p.getImageUri());
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    Timber.d("file Deleted: %s", p.getImageUri());
                                } else {
                                    Timber.d("file not Deleted: %s", p.getImageUri());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Error in deleting ebay images");

                    }
                });
    }

}
