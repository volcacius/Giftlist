package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.domain.interactor.GetDbProductListUseCase;
import it.polimi.dima.giftlist.domain.interactor.GetWishlistUseCase;
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

    //used by wishlist
    @Provides
    @PerActivity
    GetDbProductListUseCase provideDbProductListUseCase(StorIOSQLite db) {
        return new GetDbProductListUseCase(db, wishlistId);
    }

    //Used by wishlist settings
    @Provides
    @PerActivity
    GetWishlistUseCase providesWishlistUseCase(StorIOSQLite db) {
        return new GetWishlistUseCase(db, wishlistId);
    }

    @Provides
    @PerActivity
    WishlistAdapter providesWishlistAdapter(Picasso picasso) {
        return new WishlistAdapter(context, picasso);
    }
}
