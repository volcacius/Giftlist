package it.polimi.dima.giftlist.presentation.presenter;

import android.database.Cursor;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Product;
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
    @DebugLog
    public void subscribe(boolean pullToRefresh) {
        if(!useCase.isUnsubscribed()) {
            unsubscribe();
        }
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    @Override
    @DebugLog
    protected void onCompleted() {
        //DB subscriptions does not complete
    }

    @Override
    @DebugLog
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    @DebugLog
    protected void onNext(Wishlist data) {
        getView().setData(data);
        if (isViewAttached()) {
            getView().showContent();
        }
    }

    @DebugLog
    public int getStartingProductDisplayOrder(long wishlistId) {
        int ebayMax = Product.DEFAULT_DISPLAY_ORDER;
        int etsyMax = Product.DEFAULT_DISPLAY_ORDER;
        Cursor ebayCursor = db.get().cursor()
                .withQuery(RawQuery.builder()
                        .query(EbayProductTable.getMaxProductDisplayOrderQuery(wishlistId))
                        .build())
                .prepare()
                .executeAsBlocking();
        if(ebayCursor.getCount() > 0 && ebayCursor.moveToFirst()) {
            //Get the first and only column
            ebayMax = ebayCursor.getInt(0);
            ebayCursor.close();
        }
        Cursor etsyCursor = db.get().cursor()
                .withQuery(RawQuery.builder()
                        .query(EtsyProductTable.getMaxProductDisplayOrderQuery(wishlistId))
                        .build())
                .prepare()
                .executeAsBlocking();
        if(etsyCursor.getCount() > 0 && etsyCursor.moveToFirst()) {
            //Get the first and only column
            etsyMax = etsyCursor.getInt(0);
        }

        Timber.d("Ebay order is %d, Etsy order is %d", ebayMax, etsyMax);
        //If no rows are present, the query return 0
        return Math.max(ebayMax, etsyMax) + 1;
    }

    @DebugLog
    public void putWishlist(Wishlist wishlist) {
        db.put()
                .object(wishlist)
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<PutResult>() {
                    @Override
                    public void onSuccess(PutResult value) {
                        Timber.d("Wishlist update/creation success");
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Wishlist update/creation error");
                    }
                });
    }
}
