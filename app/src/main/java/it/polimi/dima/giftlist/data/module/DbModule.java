package it.polimi.dima.giftlist.data.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.db.DbOpenHelper;
import it.polimi.dima.giftlist.data.db.resolver.CurrencyDeleteResolver;
import it.polimi.dima.giftlist.data.db.resolver.CurrencyGetResolver;
import it.polimi.dima.giftlist.data.db.resolver.CurrencyPutResolver;
import it.polimi.dima.giftlist.data.db.resolver.EtsyProductDeleteResolver;
import it.polimi.dima.giftlist.data.db.resolver.EtsyProductGetResolver;
import it.polimi.dima.giftlist.data.db.resolver.EtsyProductPutResolver;
import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.EtsyProduct;

/**
 * Created by Alessandro on 29/03/16.
 */

@Module
public class DbModule {

    @Provides
    @Singleton
    public StorIOSQLite provideStorIOSQLite(SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(Currency.class, SQLiteTypeMapping.<Currency>builder()
                        .putResolver(new CurrencyPutResolver())
                        .getResolver(new CurrencyGetResolver())
                        .deleteResolver(new CurrencyDeleteResolver())
                        .build())
                .addTypeMapping(EtsyProduct.class, SQLiteTypeMapping.<EtsyProduct>builder()
                        .putResolver(new EtsyProductPutResolver())
                        .getResolver(new EtsyProductGetResolver())
                        .deleteResolver(new EtsyProductDeleteResolver())
                        .build())
                .build();
    }

    @Provides
    @Singleton
    public SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        return new DbOpenHelper(context);
    }
}
