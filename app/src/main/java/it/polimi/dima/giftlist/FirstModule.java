package it.polimi.dima.giftlist;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.wishlistlist.ErrorMessageDeterminer;
import it.polimi.dima.giftlist.wishlistlist.WishlistListAdapter;

/**
 * Created by Elena on 12/01/2016.
 */
@Module(
)
public class FirstModule {

    private Context context;

    public FirstModule(Context context) {
        this.context = context;
    }

    @Provides public Context provideContext() {
        return context;
    }

    @Provides @Singleton public DummyInterface providesDummyInterface() {
        return new DummyList();
    }

    @Provides
    @Singleton
    public ErrorMessageDeterminer providesErrorMessageDeterminer(){
        return new ErrorMessageDeterminer();
    }
}
