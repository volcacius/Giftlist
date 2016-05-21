package it.polimi.dima.giftlist.domain.interactor;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import rx.Observable;

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetWishlistListUseCase extends UseCase<List<Wishlist>> {

    EventBus eventBus;
    StorIOSQLite db;

    @Inject
    public GetWishlistListUseCase(EventBus eventBus, StorIOSQLite db) {
        this.eventBus = eventBus;
        this.db = db;
    }

    @Override
    protected Observable<List<Wishlist>> buildUseCaseObservable() {
        return db.get()
                .listOfObjects(Wishlist.class)
                .withQuery(Query.builder()
                        .table(WishlistTable.TABLE)
                        .build())
                .prepare()
                .asRxObservable();
    }
}
