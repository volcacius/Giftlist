package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;


import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistListAdapter;

/**
 * Created by Elena on 12/01/2016.
 */
@Module()
public class WishlistListModule {

    private Context context;
    public WishlistListModule(Context context) {
        this.context = context;
    }

    @Provides
    @PerActivity
    public Context provideContext() {
        return context;
    }

    @Provides
    @PerActivity
    public WishlistListAdapter providesWishlistListAdapter() {
        return new WishlistListAdapter(context);
    }


}
