package it.polimi.dima.giftlist.wishlistlist;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;
import it.polimi.dima.giftlist.FirstModule;

/**
 * Created by Elena on 12/01/2016.
 */
@Singleton @Component(
        modules = FirstModule.class)
public interface WishlistListComponent {

    public void inject(WishlistListFragment fragment); //allows injecting non-private field members to provided object as argument

    public WishlistListPresenter presenter(); //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.

    public WishlistListAdapter provideAdapter();
}


