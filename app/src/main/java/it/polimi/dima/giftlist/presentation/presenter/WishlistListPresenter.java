package it.polimi.dima.giftlist.presentation.presenter;

import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
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
        getView().setData(data);
        if (isViewAttached()) {
            getView().showContent();
        }
    }

    public void updateWishlistList(List<Wishlist> wishlistList) {
        db.put()
                .objects(wishlistList)
                .prepare()
                .executeAsBlocking();
    }


    public void removeWishlist(Wishlist wishlist) {

        long wishlistId = wishlist.getId();
        Timber.d("deleting wishlist " +wishlistId);
        deleteImages(wishlistId);

        db.delete()
                .byQuery(DeleteQuery.builder()
                        .table(WishlistTable.TABLE)
                        .where(WishlistTable.COLUMN_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .executeAsBlocking();
        db.delete()
                .byQuery(DeleteQuery.builder()
                        .table(EtsyProductTable.TABLE)
                        .where(EtsyProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .executeAsBlocking();
        db.delete()
                .byQuery(DeleteQuery.builder()
                        .table(EbayProductTable.TABLE)
                        .where(EbayProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .executeAsBlocking();
    }




    private void deleteImages(long wishlistId) {

        final List<EtsyProduct> etsyDelete = db
                .get()
                .listOfObjects(EtsyProduct.class)
                .withQuery(Query.builder()
                        .table(EtsyProductTable.TABLE)
                        .where(EtsyProductTable.COLUMN_WISHLIST_ID + " = ?")

                        .build())
                .prepare()
                .executeAsBlocking();

        for (EtsyProduct p : etsyDelete) {
            File fdelete = new File(p.getImageUri());
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Timber.d("file Deleted :" + p.getImageUri());
                } else {
                    Timber.d("file not Deleted :" + p.getImageUri());
                }
            }
        }

        List<EbayProduct> ebayDelete =
                db.get()
                        .listOfObjects(EbayProduct.class)
                        .withQuery(Query.builder()
                                .table(EbayProductTable.TABLE)
                                .where(EbayProductTable.COLUMN_WISHLIST_ID + " = ?")
                                .whereArgs(wishlistId)
                                .build())
                        .prepare()
                        .executeAsBlocking();

        for (EbayProduct p : ebayDelete) {
            File fdelete = new File(p.getImageUri());
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Timber.d("file Deleted :" + p.getImageUri());
                } else {
                    Timber.d("file not Deleted :" + p.getImageUri());
                }
            }
        }
    }

}
