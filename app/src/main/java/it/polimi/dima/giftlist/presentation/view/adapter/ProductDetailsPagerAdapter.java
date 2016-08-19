package it.polimi.dima.giftlist.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductDetailsFragmentBuilder;
import timber.log.Timber;


/**
 * Created by Alessandro on 10/08/16.
 */
public class ProductDetailsPagerAdapter extends FragmentStatePagerAdapter {

    List<Product> productList;

    @DebugLog
    public ProductDetailsPagerAdapter(FragmentManager fm, List<Product> productList) {
        super(fm);
        this.productList = productList;
    }

    @Override
    @DebugLog
    public Fragment getItem(int position) {
        Timber.d("Get item at position " + position);
        return new ProductDetailsFragmentBuilder(productList.get(position)).build();
    }

    @Override
    public int getCount() {
        return productList.size();
    }
}
