package it.polimi.dima.giftlist.presentation.component;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.presentation.module.ApplicationModule;
import it.polimi.dima.giftlist.presentation.module.ProductListModule;
import it.polimi.dima.giftlist.presentation.presenter.ProductListPresenter;
import it.polimi.dima.giftlist.presentation.view.fragment.EtsyProductListFragment;
import it.polimi.dima.giftlist.scope.PerActivity;


/**
 * Created by Elena on 27/01/2016.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = {ProductListModule.class})
public interface ProductListComponent {

    void inject(EtsyProductListFragment fragment); //allows injecting non-private field members to provided object as argument

    ProductListPresenter providePresenter(); //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.

}