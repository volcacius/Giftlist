package it.polimi.dima.giftlist.data.db.resolver;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EtsyProduct;

/**
 * Created by Alessandro on 29/03/16.
 */
public class EtsyProductGetResolver extends DefaultGetResolver<EtsyProduct> {

    @NonNull
    @Override
    public EtsyProduct mapFromCursor(Cursor cursor) {
        return new EtsyProduct(
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
                CurrencyType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("currency_type"))),
                cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
        );
    }
}
