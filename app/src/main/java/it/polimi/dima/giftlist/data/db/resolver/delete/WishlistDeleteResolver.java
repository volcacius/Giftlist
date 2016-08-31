package it.polimi.dima.giftlist.data.db.resolver.delete;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import it.polimi.dima.giftlist.data.db.table.CurrencyTable;
import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.Wishlist;
import timber.log.Timber;

/**
 * Created by Alessandro on 29/03/16.
 */
public class WishlistDeleteResolver extends DefaultDeleteResolver<Wishlist> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull Wishlist object) {
        Timber.d("delete resolver "+ object.getId());
        return DeleteQuery.builder()
                .table(WishlistTable.TABLE)
                .where(WishlistTable.COLUMN_ID + "= ?")
                .whereArgs(object.getId())
                .build();
    }
}
