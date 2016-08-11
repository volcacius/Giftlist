package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.presentation.presenter.WishlistSettingsPresenter;

/**
 * Created by Elena on 10/08/2016.
 */
@Module
public class WishlistSettingsModule {

    private static final long NULL_ID = 0;

    private Context context;
    long wishlistId = NULL_ID;

    public WishlistSettingsModule(Context context, long wishlistId) {
        this.context = context;
        this.wishlistId = wishlistId;
    }

    @Provides
    public Context provideContext() {
        return context;
    }


}
