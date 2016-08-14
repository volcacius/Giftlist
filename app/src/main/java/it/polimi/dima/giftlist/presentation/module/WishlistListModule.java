package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.DummyInterface;
import it.polimi.dima.giftlist.data.DummyList;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

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
