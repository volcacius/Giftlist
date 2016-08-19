package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
public class WishlistFragment extends BaseMvpLceFragment<RecyclerView, List<Product>, WishlistView, WishlistPresenter>
        implements WishlistView {

    @Bind(R.id.contentView)
    RecyclerView recyclerView;

    @Inject
    WishlistAdapter wishlistAdapter;
    @Inject
    IntentStarter intentStarter;

    @Arg
    long wishlistId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wishlist;
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(WishlistComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.setOnProductClickListener(new WishlistAdapter.OnProductClickListener() {
            @Override
            public void onItemClick(View v , int position) {
                intentStarter.startProductDetailsPagerActivity(getContext(), wishlistAdapter.getProductList(), wishlistAdapter.getItem(position).getId());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public WishlistPresenter createPresenter() {
        return this.getComponent(WishlistComponent.class).provideWishlistPresenter();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Timber.d("button add pressed");
                intentStarter.startProductPickerSettingsActivity(getContext(), wishlistId);
                return true;

            case R.id.action_settings:
                Timber.d("button settings pressed");
                intentStarter.startWishlistSettingsActivity(getContext(), wishlistId);
                return true;

            default:
                break;

        }
        return false;
    }

    @Override
    public void removeProduct(Product product) {
    }



}
