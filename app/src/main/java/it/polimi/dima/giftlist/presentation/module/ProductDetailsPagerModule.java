package it.polimi.dima.giftlist.presentation.module;

import android.support.v4.app.FragmentManager;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductDetailsPagerAdapter;

/**
 * Created by Alessandro on 11/08/16.
 */
@Module
public class ProductDetailsPagerModule {

    private FragmentManager fragmentManager;
    private List<Product> productList;

    public ProductDetailsPagerModule(FragmentManager fragmentManager, List<Product> productList) {
        this.fragmentManager = fragmentManager;
        this.productList = productList;
    }

    @Provides
    @PerActivity
    ProductDetailsPagerAdapter providesProductDetailsAdapter() {
        return new ProductDetailsPagerAdapter(fragmentManager, productList);
    }
}
