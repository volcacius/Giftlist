package it.polimi.dima.giftlist.presentation.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.OnClick;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.view.ProductListView;
import it.polimi.dima.giftlist.presentation.component.ProductListComponent;
import it.polimi.dima.giftlist.presentation.module.ProductListModule;
import it.polimi.dima.giftlist.presentation.presenter.ProductListPresenter;
import it.polimi.dima.giftlist.presentation.view.activity.ProductListActivity;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

/**
 * Created by Elena on 19/01/2016.
 */
@FragmentWithArgs
public class ProductListFragment extends BaseViewStateLceFragment<SwipeRefreshLayout, List<Product>, ProductListView, ProductListPresenter>
        implements ProductListView, SwipeRefreshLayout.OnRefreshListener {

    private static final int LOADING_OFFSET = 1;
    private static final int IMAGE_WIDTH = 240;
    private static final int IMAGE_HEIGHT = 330;

    @Bind(R.id.product_title)
    TextView mTitleTextView;

    @Bind(R.id.next_button)
    ImageButton mNextProduct;

    @Bind(R.id.product_thumb)
    ImageView mProductThumb;

    @Bind(R.id.product_price)
    TextView mPriceTextView;

    @Bind(R.id.product_price_converted)
    TextView mConvertedPriceTextView;

    @Bind(R.id.reload_button)
    Button mReloadButton;

    @BindColor(R.color.primary)
    int mColorPrimary;

    @BindColor(R.color.gray)
    int mColorGray;

    @Arg
    String category;
    @Arg
    String keywords;

    @Inject
    ErrorMessageDeterminer errorMessageDeterminer;
    @Inject
    Picasso picasso;
    @Inject
    IntentStarter intentStarter;

    List<Product> productList;
    int currentIndex;
    Product currentProduct;

    @Override
    protected void injectDependencies() {
        this.getComponent(ProductListComponent.class).inject(this);
    }

    private void populateProductViews() {
        mTitleTextView.setText(currentProduct.getName());
        mPriceTextView.setText(currentProduct.getPrice() + " " + currentProduct.getCurrency());
        if(currentProduct.getConvertedPrice()!= 0 && currentProduct.getCurrency()!= "EUR") {
            mConvertedPriceTextView.setText("(" + currentProduct.getConvertedPrice() + " EUR)");
        }
        if (currentProduct.getImageUrl() == null) {
            ColorDrawable colorDrawable = new ColorDrawable(mColorPrimary);
            mProductThumb.setDrawingCacheEnabled(true);
            mProductThumb.setImageDrawable(colorDrawable);

        } else {
            picasso.load(currentProduct.getImageUrl())
                    .resize(IMAGE_WIDTH,IMAGE_HEIGHT)
                    .centerCrop()
                    .into(mProductThumb);
        }
    }

    @OnClick(R.id.next_button)
    public void goNext() {
        currentIndex++;
        currentProduct = productList.get(currentIndex);
        if (currentIndex == productList.size() - LOADING_OFFSET) {
            loadData(true);
        }
        populateProductViews();
    }

    @OnClick(R.id.reload_button)
    public void reloadItems() {
        loadData(true);
    }

    @NonNull
    @Override
    public ProductListPresenter createPresenter() {
        return this.getComponent(ProductListComponent.class).providePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView.setOnRefreshListener(this);
        //previously: loaddata
    }

    @NonNull
    @Override
    public LceViewState<List<Product>, ProductListView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Product> getData() {
        return null;// mProductAdapter.getEtsyProductList();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorMessageDeterminer.getErrorMessage(e, pullToRefresh);
    }

    @Override
    public void setData(List<Product> data) {
        if (data != null) {
            this.productList = data;
            currentIndex = 0;

        }
        if(productList.size()>0) {
            if (currentProduct == null) {
                currentProduct = productList.get(currentIndex);
            }
            populateProductViews();
        } else {
            Toast.makeText(getContext(), "no results found, new general request issued", Toast.LENGTH_LONG).show();
            keywords="";
            presenter.subscribe(true);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.subscribe(pullToRefresh);

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
        mReloadButton.setVisibility(View.VISIBLE);
        mReloadButton.setEnabled(true);
        e.printStackTrace();
    }

    @Override
    public void showContent() {
        super.showContent();
        mNextProduct.setEnabled(true);
        mReloadButton.setVisibility(View.GONE);
        mReloadButton.setEnabled(false);
        contentView.setRefreshing(false);
    }

    //called by BaseRxLcePresenter in method subscribe()
    @Override
    public void showLoading(boolean pullToRefresh) {
        mNextProduct.setEnabled(false);
        super.showLoading(pullToRefresh);
        /*
        if (pullToRefresh && !contentView.isRefreshing()) {
            // Workaround for measure bug: https://code.google.com/p/android/issues/detail?id=77712
            contentView.post(new Runnable() {
                @Override public void run() {
                    contentView.setRefreshing(true);
                }
            });
        }
        */
    }

    @Override
    public void onRefresh() {
        //TODO check why it is triggered also on click :S
        loadData(true);
    }
}
