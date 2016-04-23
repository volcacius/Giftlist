package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.presentation.module.ProductListModule;
import it.polimi.dima.giftlist.presentation.presenter.ProductListPresenter;
import it.polimi.dima.giftlist.presentation.view.activity.ProductListActivity;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductListAdapter;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductListFragment;


/**
 * Created by Elena on 27/01/2016.
 */
@PerActivity
@Subcomponent(modules = {ProductListModule.class})
public interface ProductListComponent {

    //allows injecting non-private field members to provided object as argument
    //The component is tied to the activity lifecycle, but I want to inject stuff into the fragment, not the activity
    void inject(ProductListFragment fragment);
    void inject(ProductListAdapter adapter);

    //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.
    ProductListPresenter provideProductListPresenter();

}
