package it.polimi.dima.giftlist.domain.interactor;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import rx.Observable;
import rx.functions.Func2;
import timber.log.Timber;

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetDbProductListUseCase extends UseCase<List<Product>> {

    StorIOSQLite db;
    long wishlistId;

    @Inject
    public GetDbProductListUseCase(StorIOSQLite db, long wishlistId) {
        this.db = db;
        this.wishlistId = wishlistId;
    }

    @Override
    protected Observable<List<Product>> buildUseCaseObservable() {
        return getWishlistEbayEtsyProductList(db, wishlistId);
    }

    public static Observable<List<Product>> getWishlistEbayEtsyProductList(StorIOSQLite db, long wishlistId) {
        return Observable.combineLatest(getWishlistEtsyProductList(db, wishlistId),
                getWishlistEbayProductList(db, wishlistId),
                new Func2<List<EtsyProduct>, List<EbayProduct>, List<Product>>() {
                    @Override
                    public List<Product> call(List<EtsyProduct> etsyProducts, List<EbayProduct> ebayProducts) {
                        List<Product> products = new ArrayList<Product>();
                        products.addAll(etsyProducts);
                        products.addAll(ebayProducts);
                        return products;
                    }
                });
    }

    public static Observable<List<EbayProduct>> getWishlistEbayProductList(StorIOSQLite db, long wishlistId) {
        Timber.d("useCase getEbayList");
        return db.get()
                .listOfObjects(EbayProduct.class)
                .withQuery(Query.builder()
                        .table(EbayProductTable.TABLE)
                        .where(EbayProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxObservable();
    }

    public static Observable<List<EtsyProduct>> getWishlistEtsyProductList(StorIOSQLite db, long wishlistId) {
        Timber.d("useCase getEtsyList");
        return db.get()
                .listOfObjects(EtsyProduct.class)
                .withQuery(Query.builder()
                        .table(EtsyProductTable.TABLE)
                        .where(EtsyProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxObservable();
    }
}
