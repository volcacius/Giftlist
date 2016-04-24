package it.polimi.dima.giftlist.data.db.resolver.delete;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.model.EbayProduct;

/**
 * Created by Alessandro on 23/04/16.
 */
public class EbayProductDeleteResolver extends DefaultDeleteResolver<EbayProduct> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull EbayProduct object) {
        return DeleteQuery.builder()
                .table(EbayProductTable.TABLE)
                .where(EtsyProductTable.COLUMN_ID + "= ?")
                .whereArgs(object.getId())
                .build();
    }
}
