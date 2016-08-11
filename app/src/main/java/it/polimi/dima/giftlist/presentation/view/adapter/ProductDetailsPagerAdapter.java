package it.polimi.dima.giftlist.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductDetailsFragmentBuilder;


/**
 * Created by Alessandro on 10/08/16.
 */
public class ProductDetailsPagerAdapter extends FragmentStatePagerAdapter {

    List<Product> productList;

    public ProductDetailsPagerAdapter(FragmentManager fm, List<Product> productList) {
        super(fm);
        this.productList = productList;
    }

    @Override
    public Fragment getItem(int position) {
        return new ProductDetailsFragmentBuilder(productList.get(position)).build();
    }

    @Override
    public int getCount() {
        return productList.size();
    }
}
