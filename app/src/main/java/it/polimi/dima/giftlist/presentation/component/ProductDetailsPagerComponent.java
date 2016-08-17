package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.module.ProductDetailsPagerModule;
import it.polimi.dima.giftlist.presentation.view.activity.ProductDetailsPagerActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductDetailsFragment;

/**
 * Created by Alessandro on 11/08/16.
 */

@PerActivity
@Subcomponent(modules = {ProductDetailsPagerModule.class})
public interface ProductDetailsPagerComponent {

    void inject(ProductDetailsPagerActivity productDetailsPagerActivity);
}
