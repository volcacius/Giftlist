package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;


import com.squareup.picasso.Picasso;

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
    public WishlistListAdapter providesWishlistListAdapter(Picasso picasso) {
        return new WishlistListAdapter(context, picasso);
    }


}
