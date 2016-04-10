package it.polimi.dima.giftlist.data.db.resolver;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import it.polimi.dima.giftlist.data.model.Currency;

/**
 * Created by Alessandro on 29/03/16.
 */
public class CurrencyPutResolver extends DefaultPutResolver<Currency> {
    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(Currency object) {
        return null;
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull Currency object) {
        return null;
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Currency object) {
        return null;
    }
}
