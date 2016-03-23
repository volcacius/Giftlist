package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.view.ProductListView;
import it.polimi.dima.giftlist.presentation.component.ProductListComponent;
import it.polimi.dima.giftlist.presentation.presenter.ProductListPresenter;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductListAdapter;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

/**
 * Created by Elena on 19/01/2016.
 */
@FragmentWithArgs
public class ProductListFragment extends BaseViewStateLceFragment<SwipeFlingAdapterView, List<Product>, ProductListView, ProductListPresenter>
        implements ProductListView {

    private static final boolean NO_PULL_TO_REFRESH = false;

    @Bind(R.id.next_button)
    Button nextProduct;
    @Bind(R.id.reload_button)
    Button reloadButton;
    @Bind(R.id.contentView)
    SwipeFlingAdapterView flingContainer;

    @Arg
    String category;
    @Arg
    String keywords;

    @Inject
    ErrorMessageDeterminer errorMessageDeterminer;
    @Inject
    IntentStarter intentStarter;
    @Inject
    ProductListAdapter productListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(ProductListComponent.class).inject(this);
    }

    @OnClick(R.id.next_button)
    public void goNext() {
        productListAdapter.removeFirstProduct();
    }

    @OnClick(R.id.reload_button)
    public void reloadItems() {
        loadData(true);
    }

    @NonNull
    @Override
    public ProductListPresenter createPresenter() {
        return this.getComponent(ProductListComponent.class).provideProductListPresenter();
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
        flingContainer.setAdapter(productListAdapter);
        flingContainer.setEmptyView(loadingView);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                productListAdapter.removeFirstProduct();
            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                presenter.subscribe(NO_PULL_TO_REFRESH);
            }

            @Override
            public void onScroll(float v) {

            }
        });
    }

    @NonNull
    @Override
    public LceViewState<List<Product>, ProductListView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Product> getData() {
        return productListAdapter.getProductList();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorMessageDeterminer.getErrorMessage(e);
    }

    @Override
    public void setData(List<Product> data) {
        productListAdapter.appendProductList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.subscribe(NO_PULL_TO_REFRESH);

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, NO_PULL_TO_REFRESH);
        reloadButton.setVisibility(View.VISIBLE);
        reloadButton.setEnabled(true);
        e.printStackTrace();
    }

    @Override
    public void showContent() {
        super.showContent();
        nextProduct.setEnabled(true);
        reloadButton.setVisibility(View.GONE);
        reloadButton.setEnabled(false);
    }

    //called by BaseRxLcePresenter in method load()
    @Override
    public void showLoading(boolean pullToRefresh) {
        nextProduct.setEnabled(false);
        super.showLoading(NO_PULL_TO_REFRESH);
    }


}
