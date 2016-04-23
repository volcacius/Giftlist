package it.polimi.dima.giftlist;

import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.presentation.component.ProductListComponent;
import it.polimi.dima.giftlist.presentation.module.ProductListModule;
import it.polimi.dima.giftlist.presentation.view.activity.BaseActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.BaseFragment;

/**
 * Created by Alessandro on 24/03/16.
 * Base interface for methods shared by the debug and release application components.
 * Note that it's a POJO interface, without any kind of annotation.
 */
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);
    void inject(BaseFragment baseFragment);
    void inject(EtsyProductDataSource etsyProductDataSource);
    void inject(EbayProductDataSource ebayProductDataSource);
    void inject(CurrencyDataSource currencyDataSource);


    //Naming convention for this method is: returned type is a subcomponent class, method name is arbitrary, parameters are modules required in this subcomponent.
    ProductListComponent plus(ProductListModule module);
}
