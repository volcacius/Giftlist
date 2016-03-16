package it.polimi.dima.giftlist.presentation.component;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.presentation.module.ProductModule;
import it.polimi.dima.giftlist.presentation.presenter.ProductPresenter;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductFragment;


/**
 * Created by Elena on 27/01/2016.
 */
@Singleton
@Component(modules = ProductModule.class)
public interface ProductComponent {

    void inject(ProductFragment fragment); //allows injecting non-private field members to provided object as argument

    ProductPresenter providePresenter(); //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.

}
