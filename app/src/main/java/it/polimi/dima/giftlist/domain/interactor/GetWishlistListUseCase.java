package it.polimi.dima.giftlist.domain.interactor;

import android.support.annotation.NonNull;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetWishlistListUseCase extends UseCase<List<Wishlist>> {

    StorIOSQLite db;

    @Inject
    public GetWishlistListUseCase(StorIOSQLite db) {
        this.db = db;
    }

    @Override
    @RxLogObservable
    protected Observable<List<Wishlist>> buildUseCaseObservable() {
        return getWishlistListObservable(db);
    }

    @NonNull
    private static Observable<List<Wishlist>> getWishlistListObservable(StorIOSQLite db) {
        return db.get()
                .listOfObjects(Wishlist.class)
                .withQuery(Query.builder()
                        .table(WishlistTable.TABLE)
                        .build())
                .prepare()
                .asRxObservable();
    }
}
