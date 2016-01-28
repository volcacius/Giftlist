package it.polimi.dima.giftlist.wishlistlist;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.DummyInterface;
import it.polimi.dima.giftlist.DummyList;
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

    @Provides public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public DummyInterface providesDummyInterface() {
        return new DummyList();
    }

    @Provides
    @Singleton
    public ErrorMessageDeterminer providesErrorMessageDeterminer(){
        return new ErrorMessageDeterminer();
    }
}
