package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistView;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistAdapter;
import timber.log.Timber;

/**
 * Created by Alessandro on 24/04/16.
 */
@FragmentWithArgs
public class WishlistFragment extends BaseViewStateLceFragment<SwipeRefreshLayout, List<Product>, WishlistView, WishlistPresenter>
        implements WishlistView, SwipeRefreshLayout.OnRefreshListener {

    @Arg
    long wishlistId;

    @Inject
    WishlistAdapter wishlistAdapter;

    @Bind(R.id.fragment_wishlist_recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wishlist;
    }

    @Override
    public WishlistPresenter createPresenter() {
        return this.getComponent(WishlistComponent.class).provideWishlistPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(wishlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.setOnRefreshListener(this);
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(WishlistComponent.class).inject(this);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void removeProduct(Product product) {
    }

    @Override
    public void setData(List<Product> data) {
        Timber.d("Fragment setData");
        wishlistAdapter.setProductList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Timber.d("Fragment loadData");
        presenter.subscribe(pullToRefresh);
    }


    @Override
    public LceViewState<List<Product>, WishlistView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Product> getData() {
        return wishlistAdapter.getProductList();
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }
}
