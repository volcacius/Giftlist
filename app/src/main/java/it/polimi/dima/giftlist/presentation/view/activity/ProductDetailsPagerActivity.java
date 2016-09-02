package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.ProductDetailsPagerComponent;
import it.polimi.dima.giftlist.presentation.module.ProductDetailsPagerModule;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductDetailsPagerAdapter;
import timber.log.Timber;

/**
 * Created by Alessandro on 10/08/16.
 */
public class ProductDetailsPagerActivity extends BaseActivity implements HasComponent<ProductDetailsPagerComponent> {

    private static final String EXTRA_PRODUCT_LIST = "product_list";
    private static final String EXTRA_SELECTED_PRODUCT_ID = "selected_product_id";

    private ProductDetailsPagerComponent productDetailsPagerComponent;

    @Bind(R.id.activity_product_details_view_pager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ProductDetailsPagerAdapter productDetailsPagerAdapter;

    private List<Product> productList;
    private long selectedProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            productList = getIntent().getParcelableArrayListExtra(EXTRA_PRODUCT_LIST);
            selectedProductId = getIntent().getLongExtra(EXTRA_SELECTED_PRODUCT_ID, Product.DEFAULT_ID);
        }
        createComponent();
        injectDependencies();
        viewPager.setAdapter(new ProductDetailsPagerAdapter(getSupportFragmentManager(), productList));
        for (Product p: productList) {
            if (p.getId() == selectedProductId) {
                Timber.d("Selected product is id " + p.getId());
                viewPager.setCurrentItem(productList.indexOf(p));
                break;
            }
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_details;
    }

    public static Intent getCallingIntent(Context context, ArrayList<Product> productList, long selectedProductId) {
        Intent callingIntent = new Intent(context, ProductDetailsPagerActivity.class);
        callingIntent.putExtra(EXTRA_PRODUCT_LIST, productList);
        callingIntent.putExtra(EXTRA_SELECTED_PRODUCT_ID, selectedProductId);
        return callingIntent;
    }

    @Override
    public ProductDetailsPagerComponent getComponent() {
        return productDetailsPagerComponent;
    }

    @Override
    public void createComponent() {
        productDetailsPagerComponent = getApplicationComponent().plus(new ProductDetailsPagerModule(getSupportFragmentManager(), productList));
    }

    public void injectDependencies() {
        getComponent().inject(this);
    }
}
