package it.polimi.dima.giftlist.data.db.resolver.delete;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polimi.dima.giftlist.data.db.table.CurrencyTable;
import it.polimi.dima.giftlist.data.model.Currency;

/**
 * Created by Alessandro on 29/03/16.
 */
public class CurrencyDeleteResolver extends DefaultDeleteResolver<Currency> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull Currency object) {
        return DeleteQuery.builder()
                .table(CurrencyTable.TABLE)
                .where(CurrencyTable.COLUMN_CURRENCY_TYPE + "= ?")
                .whereArgs(object.getCurrencyType().toString())
                .build();
    }
}
