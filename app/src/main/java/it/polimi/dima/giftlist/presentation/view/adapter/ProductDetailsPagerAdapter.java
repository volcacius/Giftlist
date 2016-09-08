package it.polimi.dima.giftlist.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductDetailsFragment;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductDetailsFragmentBuilder;
import it.polimi.dima.giftlist.util.ViewUtil;
import timber.log.Timber;


/**
 * Created by Alessandro on 10/08/16.
 */
public class ProductDetailsPagerAdapter extends FragmentStatePagerAdapter {

    public static final int MAX_ELEVATION_FACTOR = 8;
    private static final int DP = 2;

    ArrayList<Product> productList;
    List<ProductDetailsFragment> productDetailsFragmentList;
    int currentPosition;
    private float baseElevation;

    @DebugLog
    public ProductDetailsPagerAdapter(FragmentManager fm, ArrayList<Product> productList) {
        super(fm);
        this.productList = productList;
        this.baseElevation = ViewUtil.dpToPx(DP);
        productDetailsFragmentList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragmentBuilder(productList.get(i)).build();
            productDetailsFragmentList.add(i, productDetailsFragment);
        }
    }

    @Override
    @DebugLog
    public ProductDetailsFragment getItem(int position) {
        return productDetailsFragmentList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        productDetailsFragmentList.set(position, (ProductDetailsFragment) fragment);
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentPosition = position;
        super.setPrimaryItem(container, position, object);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    public float getBaseElevation() {
        return baseElevation;
    }

    public CardView getCardViewAt(int position) {
        return productDetailsFragmentList.get(position).getCardView();
    }

}
