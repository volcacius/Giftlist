package it.polimi.dima.giftlist.data.db.resolver.put;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.Arrays;

import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.model.EbayProduct;

/**
 * Created by Alessandro on 23/04/16.
 */
public class EbayProductPutResolver extends DefaultPutResolver<EbayProduct> {

    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull EbayProduct object) {
        return InsertQuery.builder()
                .table(EbayProductTable.TABLE)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull EbayProduct object) {

        Object[] argsArray = {object.getPrimaryKeyId()};

        return UpdateQuery.builder()
                .table(EbayProductTable.TABLE)
                .where(EbayProductTable.COLUMN_PRIMARY_ID + " = ?")
                .whereArgs(Arrays.asList(argsArray))
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull EbayProduct object) {
        ContentValues values = new ContentValues();
        values.put(EbayProductTable.COLUMN_ID, object.getId());
        values.put(EbayProductTable.COLUMN_PRIMARY_ID, object.getPrimaryKeyId());
        values.put(EbayProductTable.COLUMN_NAME, object.getName());
        values.put(EbayProductTable.COLUMN_DESCRIPTION, object.getDescription());
        values.put(EbayProductTable.COLUMN_PRICE, object.getPrice());
        values.put(EbayProductTable.COLUMN_CONVERTED_PRICE, object.getConvertedPrice());
        values.put(EbayProductTable.COLUMN_CURRENCY_TYPE, object.getCurrencyType().toString());
        values.put(EbayProductTable.COLUMN_IMAGE_URL, object.getImageUrl());
        values.put(EbayProductTable.COLUMN_IMAGE_URI, object.getImageUri());
        values.put(EbayProductTable.COLUMN_PRODUCT_PAGE, object.getProductPage());
        values.put(EbayProductTable.COLUMN_WISHLIST_ID, object.getWishlistId());
        values.put(EbayProductTable.COLUMN_DISPLAY_ORDER, object.getDisplayOrder());
        return values;
    }
}
