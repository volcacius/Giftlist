package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.ProductDetailsPagerComponent;
import it.polimi.dima.giftlist.presentation.module.ProductDetailsPagerModule;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductDetailsPagerAdapter;

/**
 * Created by Alessandro on 10/08/16.
 */
public class ProductDetailsPagerActivity extends BaseActivity implements HasComponent<ProductDetailsPagerComponent> {

    private static final String EXTRA_PRODUCT_LIST = "product_list";
    private static final String EXTRA_SELECTED_PRODUCT = "selected_product";

    private ProductDetailsPagerComponent productDetailsPagerComponent;

    @Bind(R.id.activity_product_details_view_pager)
    ViewPager viewPager;

    @Inject
    ProductDetailsPagerAdapter productDetailsPagerAdapter;

    private List<Product> productList;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        if (savedInstanceState == null) {
            productList = getIntent().getParcelableArrayListExtra(EXTRA_PRODUCT_LIST);
            selectedProduct = getIntent().getParcelableExtra(EXTRA_SELECTED_PRODUCT);
        }
        createComponent();
        viewPager.setAdapter(productDetailsPagerAdapter);
        viewPager.setCurrentItem(productList.indexOf(selectedProduct));
    }

    public static Intent getCallingIntent(Context context, ArrayList<Product> productList, Product selectedProduct) {
        Intent callingIntent = new Intent(context, ProductDetailsPagerActivity.class);
        callingIntent.putExtra(EXTRA_PRODUCT_LIST, productList);
        callingIntent.putExtra(EXTRA_SELECTED_PRODUCT, selectedProduct);
        return callingIntent;
    }

    @Override
    public ProductDetailsPagerComponent getComponent() {
        return productDetailsPagerComponent;
    }

    private void createComponent() {
        productDetailsPagerComponent = getApplicationComponent().plus(new ProductDetailsPagerModule(getSupportFragmentManager(), productList));
    }
}
