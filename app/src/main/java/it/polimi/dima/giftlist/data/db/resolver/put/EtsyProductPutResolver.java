package it.polimi.dima.giftlist.data.db.resolver.put;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.Arrays;

import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.model.EtsyProduct;

/**
 * Created by Alessandro on 29/03/16.
 */
public class EtsyProductPutResolver extends DefaultPutResolver<EtsyProduct> {


    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull EtsyProduct object) {
        return InsertQuery.builder()
                .table(EtsyProductTable.TABLE)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull EtsyProduct object) {

        Object[] argsArray = {object.getId(),
                              object.getName(),
                              object.getDescription(),
                              object.getPrice(),
                              object.getConvertedPrice(),
                              object.getCurrencyType().toString(),
                              object.getImageUrl(),
                              object.getImageUri(),
                              object.getProductPage(),
                              object.getWishlistId(),
                              object.getDisplayOrder(),
                              object.getPrimaryKeyId()};

        return UpdateQuery.builder()
                .table(EtsyProductTable.TABLE)
                .where(EtsyProductTable.COLUMN_ID + " = ?")
                .where(EtsyProductTable.COLUMN_NAME + " = ?")
                .where(EtsyProductTable.COLUMN_DESCRIPTION + " = ?")
                .where(EtsyProductTable.COLUMN_PRICE + " = ?")
                .where(EtsyProductTable.COLUMN_CONVERTED_PRICE + " = ?")
                .where(EtsyProductTable.COLUMN_CURRENCY_TYPE + " = ?")
                .where(EtsyProductTable.COLUMN_IMAGE_URL + " = ?")
                .where(EtsyProductTable.COLUMN_IMAGE_URI + " = ?")
                .where(EtsyProductTable.COLUMN_PRODUCT_PAGE + " = ?")
                .where(EtsyProductTable.COLUMN_WISHLIST_ID + " = ?")
                .where(EtsyProductTable.COLUMN_PRIMARY_ID + " = ?")
                .where(EtsyProductTable.COLUMN_DISPLAY_ORDER + " = ?")
                .whereArgs(Arrays.asList(argsArray))
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull EtsyProduct object) {
        ContentValues values = new ContentValues();
        values.put(EtsyProductTable.COLUMN_ID, object.getId());
        values.put(EtsyProductTable.COLUMN_PRIMARY_ID, object.getPrimaryKeyId());
        values.put(EtsyProductTable.COLUMN_NAME, object.getName());
        values.put(EtsyProductTable.COLUMN_DESCRIPTION, object.getDescription());
        values.put(EtsyProductTable.COLUMN_PRICE, object.getPrice());
        values.put(EtsyProductTable.COLUMN_CONVERTED_PRICE, object.getConvertedPrice());
        values.put(EtsyProductTable.COLUMN_CURRENCY_TYPE, object.getCurrencyType().toString());
        values.put(EtsyProductTable.COLUMN_IMAGE_URL, object.getImageUrl());
        values.put(EtsyProductTable.COLUMN_IMAGE_URI, object.getImageUri());
        values.put(EtsyProductTable.COLUMN_PRODUCT_PAGE, object.getProductPage());
        values.put(EtsyProductTable.COLUMN_WISHLIST_ID, object.getWishlistId());
        values.put(EtsyProductTable.COLUMN_DISPLAY_ORDER, object.getDisplayOrder());
        return values;
    }
}
