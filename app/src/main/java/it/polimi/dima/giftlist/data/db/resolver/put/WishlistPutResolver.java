package it.polimi.dima.giftlist.data.db.resolver.put;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.Arrays;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;
import timber.log.Timber;

/**
 * Created by Alessandro on 23/04/16.
 */
public class WishlistPutResolver extends DefaultPutResolver<Wishlist> {

    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull Wishlist object) {
        return InsertQuery.builder()
                .table(WishlistTable.TABLE)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull Wishlist object) {

        /*Object[] argsArray = {object.getId(),
                              object.getName()};*/

        return UpdateQuery.builder()
                .table(WishlistTable.TABLE)
                .where(WishlistTable.COLUMN_ID + " = ?")
                .whereArgs(object.getId())//Arrays.asList(argsArray))
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Wishlist object) {
        ContentValues values = new ContentValues();
        values.put(WishlistTable.COLUMN_ID, object.getId());
        values.put(WishlistTable.COLUMN_NAME, object.getName());
        return values;
    }
}
