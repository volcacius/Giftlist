package it.polimi.dima.giftlist.data.db.resolver.get;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;

/**
 * Created by Alessandro on 29/03/16.
 */
public class EtsyProductGetResolver extends DefaultGetResolver<EtsyProduct> {

    @NonNull
    @Override
    public EtsyProduct mapFromCursor(Cursor cursor) {
        return new EtsyProduct(
                cursor.getString(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_DESCRIPTION)),
                cursor.getLong(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_ID)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_PRICE)),
                CurrencyType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_CURRENCY_TYPE))),
                cursor.getString(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_IMAGE_URL)),
                cursor.getLong(cursor.getColumnIndexOrThrow(EtsyProductTable.COLUMN_WISHLIST_ID))
        );
    }
}
