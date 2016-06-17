package it.polimi.dima.giftlist.data.db.resolver.get;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import timber.log.Timber;

/**
 * Created by Alessandro on 23/04/16.
 */
public class EbayProductGetResolver extends DefaultGetResolver<EbayProduct> {

    @NonNull
    @Override
    public EbayProduct mapFromCursor(Cursor cursor) {
        Timber.d("useCase resolver");
        return new EbayProduct(
                cursor.getString(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_DESCRIPTION)),
                cursor.getLong(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_ID)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_PRICE)),
                CurrencyType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_CURRENCY_TYPE))),
                cursor.getString(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_IMAGE_URL)),
                cursor.getString(cursor.getColumnIndexOrThrow(EbayProductTable.COLUMN_PRODUCT_PAGE))
        );
    }
}
