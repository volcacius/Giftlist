package it.polimi.dima.giftlist.data.db.resolver;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import it.polimi.dima.giftlist.data.db.table.CurrencyTable;
import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.CurrencyType;

/**
 * Created by Alessandro on 29/03/16.
 */
public class CurrencyGetResolver extends DefaultGetResolver<Currency> {

    @NonNull
    @Override
    public Currency mapFromCursor(Cursor cursor) {
        CurrencyType currencyType = CurrencyType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(CurrencyTable.COLUMN_CURRENCY_TYPE)));
        float rate = cursor.getFloat(cursor.getColumnIndexOrThrow(CurrencyTable.COLUMN_RATE));
        return new Currency(currencyType, rate);
    }
}
