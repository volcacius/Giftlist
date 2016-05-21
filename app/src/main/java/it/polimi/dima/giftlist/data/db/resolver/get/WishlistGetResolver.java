package it.polimi.dima.giftlist.data.db.resolver.get;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Wishlist;

/**
 * Created by Alessandro on 29/03/16.
 */
public class WishlistGetResolver extends DefaultGetResolver<Wishlist> {

    @NonNull
    @Override
    public Wishlist mapFromCursor(Cursor cursor) {
        return new Wishlist(
                cursor.getLong(cursor.getColumnIndexOrThrow(WishlistTable.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(WishlistTable.COLUMN_NAME))
        );
    }
}
