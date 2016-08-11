package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import javax.inject.Inject;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductDetailsPagerAdapter;

/**
 * Created by Alessandro on 10/08/16.
 */
@FragmentWithArgs
public class ProductDetailsFragment extends BaseFragment {

    @Arg
    Product product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product_details;
    }

    @Override
    protected void injectDependencies() {
        /*no dependencies*/
    }


}
