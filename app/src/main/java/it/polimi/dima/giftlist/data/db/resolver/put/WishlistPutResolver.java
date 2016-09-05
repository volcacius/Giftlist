package it.polimi.dima.giftlist.data.db.resolver.put;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.Wishlist;

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
        return UpdateQuery.builder()
                .table(WishlistTable.TABLE)
                .where(WishlistTable.COLUMN_ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Wishlist object) {
        ContentValues values = new ContentValues();
        values.put(WishlistTable.COLUMN_ID, object.getId());
        values.put(WishlistTable.COLUMN_NAME, object.getName());
        values.put(WishlistTable.COLUMN_OCCASION, object.getOccasion());
        values.put(WishlistTable.COLUMN_DISPLAY_ORDER, object.getDisplayOrder());
        return values;
    }
}
