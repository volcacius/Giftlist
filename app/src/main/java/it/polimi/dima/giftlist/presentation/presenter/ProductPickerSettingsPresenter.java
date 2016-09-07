package it.polimi.dima.giftlist.presentation.presenter;

import android.database.Cursor;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
import rx.Observer;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.view.ProductPickerSettingsView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Elena on 22/08/2016.
 */
public class ProductPickerSettingsPresenter extends MvpBasePresenter<ProductPickerSettingsView> {

    protected StorIOSQLite db;

    @Inject
    public ProductPickerSettingsPresenter(StorIOSQLite db) {
        this.db = db;
    }

    //TODO: async rx
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
}