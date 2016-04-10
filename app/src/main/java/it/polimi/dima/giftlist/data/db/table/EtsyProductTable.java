package it.polimi.dima.giftlist.data.db.table;

/**
 * Created by Alessandro on 29/03/16.
 */
public class EtsyProductTable {
    public static final String TABLE = "etsy_product";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CURRENCY_TYPE = "currency_type";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DESCRIPTION_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_DESCRIPTION;
    public static final String COLUMN_ID_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_PRICE_TABLE_PREFIX = TABLE + "." + COLUMN_PRICE;
    public static final String COLUMN_CURRENCY_TYPE_TABLE_PREFIX = TABLE + "." + COLUMN_CURRENCY_TYPE;
    public static final String COLUMN_CATEGORY_TABLE_PREFIX = TABLE + "." + COLUMN_CATEGORY;

    // This is just class with Meta Data, we don't need instances
    private EtsyProductTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_DESCRIPTION + " TEXT NOT NULL"
                + COLUMN_PRICE + " FLOAT NOT NULL"
                + COLUMN_CURRENCY_TYPE + " TEXT NOT NULL"
                + COLUMN_CATEGORY + " INTEGER NOT NULL"
                + ");";
    }
}
