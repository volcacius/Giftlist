package it.polimi.dima.giftlist.data.db.resolver;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
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
public class CurrencyDeleteResolver extends DeleteResolver<Currency> {

    @NonNull
    @Override
    public DeleteResult performDelete(StorIOSQLite storIOSQLite, Currency object) {

        List<Object> objectsToDelete = new ArrayList<>();
        objectsToDelete.add(object);

        storIOSQLite
                .delete()
                .objects(objectsToDelete)
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(1);
        affectedTables.add(CurrencyTable.TABLE);

        return DeleteResult.newInstance(1, affectedTables);
    }
}
