package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.domain.interactor.GetDbProductListUseCase;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistAdapter;

/**
 * Created by Alessandro on 24/04/16.
 */
@Module
public class WishlistModule {

    private static final long NULL_ID = 0;

    Context context;
    long wishlistId = NULL_ID;

    public WishlistModule(Context context, long wishlistId) {
        this.context = context;
        this.wishlistId = wishlistId;
    }

    @Provides
    @PerActivity
    GetDbProductListUseCase provideDbProductListUseCase(EventBus eventBus, StorIOSQLite db) {
        return new GetDbProductListUseCase(eventBus, db, wishlistId);
    }

    @Provides
    @PerActivity
    WishlistAdapter providesWishlistAdapter() {
        return new WishlistAdapter(context);
    }
}
