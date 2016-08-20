package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;


import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.di.PerActivity;

/**
 * Created by Elena on 12/01/2016.
 */
@Module(
)
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


}
