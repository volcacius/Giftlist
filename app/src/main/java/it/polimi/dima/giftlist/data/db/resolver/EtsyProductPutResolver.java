package it.polimi.dima.giftlist.data.db.resolver;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.module.DebugEtsyModule;

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
                object.getDescription(),
                object.getPrice(),
                object.getCurrencyType(),
                object.getCategoryId()};

        return UpdateQuery.builder()
                .table(EtsyProductTable.TABLE)
                .where(EtsyProductTable.COLUMN_ID + " = ?")
                .where(EtsyProductTable.COLUMN_DESCRIPTION + " = ?")
                .where(EtsyProductTable.COLUMN_PRICE + " = ?")
                .where(EtsyProductTable.COLUMN_CURRENCY_TYPE + " = ?")
                .where(EtsyProductTable.COLUMN_CATEGORY + " = ?")
                .whereArgs(Arrays.asList(argsArray))
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull EtsyProduct object) {
        ContentValues values = new ContentValues();
        values.put(EtsyProductTable.COLUMN_ID, object.getId());
        values.put(EtsyProductTable.COLUMN_DESCRIPTION, object.getDescription());
        values.put(EtsyProductTable.COLUMN_PRICE, object.getPrice());
        values.put(EtsyProductTable.COLUMN_CURRENCY_TYPE, object.getCurrencyType().toString());
        values.put(EtsyProductTable.COLUMN_CATEGORY, object.getCategoryId());
        return values;
    }
}
