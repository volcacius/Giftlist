package it.polimi.dima.giftlist;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.data.repository.datasource.CurrencyDataSource;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import it.polimi.dima.giftlist.presentation.component.ProductListComponent;
import it.polimi.dima.giftlist.presentation.module.ApplicationModule;
import it.polimi.dima.giftlist.presentation.module.ProductListModule;
import it.polimi.dima.giftlist.presentation.view.activity.BaseActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.BaseFragment;

/**
 * Created by Alessandro on 15/03/16.
 */
//Dagger ignores the annotation put atop the @Component. I put it there just for readability
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);
    void inject(BaseFragment baseFragment);
    void inject(EtsyProductDataSource etsyProductDataSource);
    void inject(CurrencyDataSource currencyDataSource);

    //Naming convention for this method is: returned type is a subcomponent class, method name is arbitrary, parameters are modules required in this subcomponent.
    ProductListComponent plus(ProductListModule module);

    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    ProductRepository productRepository();
}
