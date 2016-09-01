package it.polimi.dima.giftlist.data.db.table;

/**
 * Created by Alessandro on 29/03/16.
 */
public class EtsyProductTable {
    public static final String TABLE = "etsy_product";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CONVERTED_PRICE = "converted_price";
    public static final String COLUMN_CURRENCY_TYPE = "currency_type";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_IMAGE_URI = "image_uri";
    public static final String COLUMN_PRODUCT_PAGE = "product_page";
    public static final String COLUMN_WISHLIST_ID = "wishlist_id";
    public static final String COLUMN_NAME_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_NAME;
    public static final String COLUMN_DESCRIPTION_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_DESCRIPTION;
    public static final String COLUMN_ID_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_PRICE_TABLE_PREFIX = TABLE + "." + COLUMN_PRICE;
    public static final String COLUMN_CONVERTED_PRICE_TABLE_PREFIX = TABLE + "." + COLUMN_PRICE;
    public static final String COLUMN_CURRENCY_TYPE_TABLE_PREFIX = TABLE + "." + COLUMN_CURRENCY_TYPE;
    public static final String COLUMN_IMAGE_URL_TABLE_PREFIX = TABLE + "." + COLUMN_IMAGE_URL;
    public static final String COLUMN_IMAGE_URI_TABLE_PREFIX = TABLE + "." + COLUMN_IMAGE_URI;
    public static final String COLUMN_PRODUCT_PAGE_TABLE_PREFIX = TABLE + "." + COLUMN_PRODUCT_PAGE;
    public static final String COLUMN_WISHLIST_ID_TABLE_PREFIX = TABLE + "." + COLUMN_WISHLIST_ID;

    // This is just class with Meta Data, we don't need instances
    private EtsyProductTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + COLUMN_PRICE + " FLOAT NOT NULL, "
                + COLUMN_CONVERTED_PRICE + " FLOAT, "
                + COLUMN_CURRENCY_TYPE + " TEXT NOT NULL, "
                + COLUMN_IMAGE_URL + " TEXT NOT NULL, "
                + COLUMN_IMAGE_URI + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_PAGE + " TEXT NOT NULL, "
                + COLUMN_WISHLIST_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY" + "(" + COLUMN_WISHLIST_ID + ") "
                + "REFERENCES " + WishlistTable.TABLE + "(" + WishlistTable.COLUMN_ID + ") "
                + ");";
    }
}
