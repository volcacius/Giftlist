package it.polimi.dima.giftlist.domain.interactor;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import org.greenrobot.eventbus.EventBus;

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

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetDbProductListUseCase extends UseCase<List<Product>> {

    EventBus eventBus;
    StorIOSQLite db;
    long wishlistId;

    @Inject
    public GetDbProductListUseCase(EventBus eventBus, StorIOSQLite db, long wishlistId) {
        this.eventBus = eventBus;
        this.db = db;
        this.wishlistId = wishlistId;
    }

    @Override
    protected Observable<List<Product>> buildUseCaseObservable() {
        return Observable.concat(getWishlistEbayProductList(),
                                 getWishlistEtsyProductList());
    }

    private Observable<List<Product>> getWishlistEbayProductList() {
        return db.get()
                .listOfObjects(EbayProduct.class)
                .withQuery(Query.builder()
                        .table(EbayProductTable.TABLE)
                        .where(EbayProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxObservable()
                .flatMap(ebayProducts -> Observable.from(ebayProducts))
                .cast(Product.class)
                .toList();
    }

    private Observable<List<Product>> getWishlistEtsyProductList() {
        return db.get()
                .listOfObjects(EtsyProduct.class)
                .withQuery(Query.builder()
                        .table(EtsyProductTable.TABLE)
                        .where(EtsyProductTable.COLUMN_WISHLIST_ID + "= ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxObservable()
                .flatMap(etsyProducts -> Observable.from(etsyProducts))
                .cast(Product.class)
                .toList();
    }
}
