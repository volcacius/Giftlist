package it.polimi.dima.giftlist.product;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by Elena on 27/01/2016.
 */
@Singleton
@Component(modules = ProductModule.class)
public interface ProductComponent {

    void inject(ProductFragment fragment); //allows injecting non-private field members to provided object as argument

    ProductPresenter providePresenter(); //returned object is an instance created by Dagger2 and all dependencies are provided by constructor injection.

    ProductAdapter provideAdapter();
}
