package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.presentation.module.ProductPickerModule;
import it.polimi.dima.giftlist.presentation.presenter.ProductPickerPresenter;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductPickerAdapter;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerFragment;


/**
 * Created by Elena on 27/01/2016.
 */
@PerActivity
@Subcomponent(modules = {ProductPickerModule.class})
public interface ProductPickerComponent {

    //allows injecting non-private field members to provided object as argument
    //The component is tied to the activity lifecycle, but I want to inject stuff into the fragment, not the activity
    void inject(ProductPickerFragment fragment);
    void inject(ProductPickerAdapter adapter);

    //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.
    ProductPickerPresenter provideProductListPresenter();

}
