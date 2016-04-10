package it.polimi.dima.giftlist.data.db.table;

/**
 * Created by Alessandro on 29/03/16.
 */
public class CurrencyTable {

    public static final String TABLE = "currency";
    public static final String COLUMN_CURRENCY_TYPE = "currency_type";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_CURRENCY_TYPE_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_CURRENCY_TYPE;
    public static final String COLUMN_RATE_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_RATE;

    // This is just class with Meta Data, we don't need instances
    private CurrencyTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_CURRENCY_TYPE + " TEXT NOT NULL PRIMARY KEY, "
                + COLUMN_RATE + " TEXT NOT NULL"
                + ");";
    }
}
