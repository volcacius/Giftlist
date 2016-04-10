package it.polimi.dima.giftlist.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.polimi.dima.giftlist.data.db.table.CurrencyTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;

/**
 * Created by Alessandro on 29/03/16.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String GIFTLIST_DB = "giftlist_db";

    public DbOpenHelper(Context context) {
        super(context, GIFTLIST_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CurrencyTable.getCreateTableQuery());
        db.execSQL(EtsyProductTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no impl
    }
}