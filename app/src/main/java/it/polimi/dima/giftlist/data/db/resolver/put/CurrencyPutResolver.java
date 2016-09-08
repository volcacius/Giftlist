package it.polimi.dima.giftlist.data.db.resolver.put;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.Arrays;

import it.polimi.dima.giftlist.data.db.table.CurrencyTable;
import it.polimi.dima.giftlist.data.model.Currency;

/**
 * Created by Alessandro on 29/03/16.
 */
public class CurrencyPutResolver extends DefaultPutResolver<Currency> {
    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(Currency object) {
        return InsertQuery.builder()
                .table(CurrencyTable.TABLE)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull Currency object) {
        Object[] argsArray = {object.getCurrencyType().toString()};

        return UpdateQuery.builder()
                .table(CurrencyTable.TABLE)
                .where(CurrencyTable.COLUMN_CURRENCY_TYPE + " = ?")
                .whereArgs(Arrays.asList(argsArray))
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Currency object) {
        ContentValues values = new ContentValues();
        values.put(CurrencyTable.COLUMN_CURRENCY_TYPE, object.getCurrencyType().toString());
        values.put(CurrencyTable.COLUMN_RATE, object.getRate());
        return values;
    }
}
