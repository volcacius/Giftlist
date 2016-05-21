package it.polimi.dima.giftlist.data.db.table;

import it.polimi.dima.giftlist.data.model.EbayProduct;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistTable {

    public static final String TABLE = "wishlist";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_NAME_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_NAME;


    // This is just class with Meta Data, we don't need instances
    private WishlistTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL"
                + ");";
    }

}
