package it.polimi.dima.giftlist.data.db.table;


import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import it.polimi.dima.giftlist.data.model.Wishlist;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistTable {

    public static final String TABLE = "wishlist";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_OCCASION = "occasion";
    public static final String COLUMN_DISPLAY_ORDER = "display_order";
    public static final String COLUMN_ID_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_NAME_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_NAME;
    public static final String COLUMN_OCCASION_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_OCCASION;

    // This is just class with Meta Data, we don't need instances
    private WishlistTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_OCCASION + " TEXT NOT NULL, "
                + COLUMN_DISPLAY_ORDER + " INTEGER NOT NULL "
                + ");";
    }

    public static String getMaxDisplayOrderQuery() {
        return "SELECT MAX(" + COLUMN_DISPLAY_ORDER + ") + 1 FROM " + TABLE;
    }

    public static String getCustomPutQuery(long id, String wishlistName, String wishlistOccasion, int displayOrder) {
        return "INSERT INTO " + TABLE + " ("
                + COLUMN_ID + ", "
                + COLUMN_NAME + ", "
                + COLUMN_OCCASION + ", "
                + COLUMN_DISPLAY_ORDER + ") "
                + "VALUES ("
                + "'" + id + "', "
                + "'" + wishlistName + "', "
                + "'" + wishlistOccasion + "', "
                + "'" + displayOrder + "'"
                +");";

    }

    public static String getNameOccasionUpdateQuery(long wishlistId, String wishlistName, String occasion) {
        return "UPDATE " + TABLE + " "
        + "SET "
        + COLUMN_NAME + "='" + wishlistName + "',"
        + COLUMN_OCCASION + "='" + occasion + "' "
        + "WHERE "
        + COLUMN_ID + "='" + wishlistId +"';";
    }

    public static String getCustomDeleteQuery(long wishlistId) {
        return "DELETE FROM " + TABLE + " "
                + "WHERE "
                + COLUMN_ID + "='" + wishlistId +"';";
    }

    public static String getDisplayOrderUpdateQuery(long wishlistId, int displayOrder) {
        return "UPDATE " + TABLE + " "
                + "SET "
                + COLUMN_DISPLAY_ORDER + "='" + displayOrder + "' "
                + "WHERE "
                + COLUMN_ID + "='" + wishlistId +"';";
    }
}
