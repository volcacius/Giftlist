package it.polimi.dima.giftlist.presentation.component;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.presentation.module.ApplicationModule;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistListFragment;
import it.polimi.dima.giftlist.presentation.module.WishlistListModule;
import it.polimi.dima.giftlist.presentation.presenter.WishlistListPresenter;
import it.polimi.dima.giftlist.scope.PerActivity;

/**
 * Created by Elena on 12/01/2016.
 */
//Dagger ignores the annotation put atop the @Component. I put it there just for readability
@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = {WishlistListModule.class})
public interface WishlistListComponent {

    //public void inject(WishlistListFragment fragment); //allows injecting non-private field members to provided object as argument

    //public WishlistListPresenter providePresenter(); //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.

    //public WishlistListAdapter provideAdapter();
}


