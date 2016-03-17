package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.component.DaggerWishlistListComponent;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistListAdapter;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;
import it.polimi.dima.giftlist.presentation.module.WishlistListModule;
import it.polimi.dima.giftlist.presentation.presenter.WishlistListPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListFragment extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Wishlist>, WishlistListView, WishlistListPresenter>
        implements WishlistListView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.fragment_wishlistlist_recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    ErrorMessageDeterminer errorMessageDeterminer;

    WishlistListAdapter mWishlistListAdapter;
    WishlistListComponent mWishlistListComponent;

    protected void injectDependencies() {
        mWishlistListComponent =
                DaggerWishlistListComponent.builder().wishlistListModule(new WishlistListModule(getActivity())).build();
        mWishlistListComponent.inject(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        injectDependencies();
        //ButterKnife is done in onViewCreate, and also setLayoutManager
        return inflater.inflate(R.layout.fragment_whislistlist, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mWishlistListAdapter = mWishlistListComponent.provideAdapter();
        mRecyclerView.setAdapter(mWishlistListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.setOnRefreshListener(this);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override public LceViewState<List<Wishlist>, WishlistListView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override public List<Wishlist> getData() {
        return mWishlistListAdapter.getWishlistList();
    }

    @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorMessageDeterminer.getErrorMessage(e, pullToRefresh);
    }

    @Override public WishlistListPresenter createPresenter() {
        return mWishlistListComponent.providePresenter();
    }

    @Override public void setData(List<Wishlist> data) {
        mWishlistListAdapter.setWishlistList(data);
        mWishlistListAdapter.notifyDataSetChanged();
    }

    @Override public void loadData(boolean pullToRefresh) {
        presenter.loadWishlistList(pullToRefresh);
    }

    @Override public void onRefresh() {
        loadData(true);
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
        e.printStackTrace();
    }

    @Override public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        if (pullToRefresh && !contentView.isRefreshing()) {
            // Workaround for measure bug: https://code.google.com/p/android/issues/detail?id=77712
            contentView.post(new Runnable() {
                @Override public void run() {
                    contentView.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void addWishlist(int wishlistId) {

    }

    @Override
    public void removeWishlist(int wishlistId) {

    }

    @Override
    public void showAddingFailed(Wishlist wishlist) {

    }

    @Override
    public void showRemovingFailed(Wishlist wishlist) {

    }
}
