package it.polimi.dima.giftlist.domain.interactor;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;
import rx.Observable;

/**
 * Created by Alessandro on 04/09/16.
 */
public class GetWishlistUseCase extends UseCase<Wishlist> {

    StorIOSQLite db;
    long wishlistId;

    @Inject
    public GetWishlistUseCase(StorIOSQLite db, long wishlistId) {
        this.db = db;
        this.wishlistId = wishlistId;
    }

    @Override
    protected Observable<Wishlist> buildUseCaseObservable() {
        return db.get()
                .object(Wishlist.class)
                .withQuery(Query.builder()
                        .table(WishlistTable.TABLE)
                        .where("id = ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxObservable();
    }
}
