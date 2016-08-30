package it.polimi.dima.giftlist;

import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.presentation.component.ProductDetailsPagerComponent;
import it.polimi.dima.giftlist.presentation.component.ProductPickerComponent;
import it.polimi.dima.giftlist.presentation.component.ProductPickerSettingsComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistSettingsComponent;
import it.polimi.dima.giftlist.presentation.module.ProductDetailsPagerModule;
import it.polimi.dima.giftlist.presentation.module.ProductPickerModule;
import it.polimi.dima.giftlist.presentation.module.ProductPickerSettingsModule;
import it.polimi.dima.giftlist.presentation.module.WishlistListModule;
import it.polimi.dima.giftlist.presentation.module.WishlistModule;
import it.polimi.dima.giftlist.presentation.module.WishlistSettingsModule;
import it.polimi.dima.giftlist.presentation.view.activity.WelcomeActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.BaseFragment;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductDetailsFragment;

/**
 * Created by Alessandro on 24/03/16.
 * Base interface for methods shared by the debug and release application components.
 * Note that it's a POJO interface, without any kind of annotation.
 */
public interface ApplicationComponent {

    void inject(WelcomeActivity welcomeActivity);
    void inject(BaseFragment baseFragment);
    void inject(ProductDetailsFragment productDetailsFragment);
    void inject(EtsyProductDataSource etsyProductDataSource);
    void inject(EbayProductDataSource ebayProductDataSource);
    void inject(CurrencyDataSource currencyDataSource);

    //Naming convention for this method is: returned type is a subcomponent class, method name is arbitrary, parameters are modules required in this subcomponent.
    ProductPickerComponent plus(ProductPickerModule module);
    WishlistComponent plus(WishlistModule module);
    WishlistListComponent plus(WishlistListModule module);
    WishlistSettingsComponent plus(WishlistSettingsModule module);
    ProductDetailsPagerComponent plus(ProductDetailsPagerModule module);
    ProductPickerSettingsComponent plus(ProductPickerSettingsModule module);
}
