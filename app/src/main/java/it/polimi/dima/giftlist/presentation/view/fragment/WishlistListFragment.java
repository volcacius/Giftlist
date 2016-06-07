
package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.event.WishlistAddedEvent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistListPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistListAdapter;
import it.polimi.dima.giftlist.util.ToastFactory;
import timber.log.Timber;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListFragment extends BaseViewStateLceFragment<RecyclerView, List<Wishlist>, WishlistListView, WishlistListPresenter>
        implements WishlistListView {

    @Bind(R.id.contentView)
    RecyclerView recyclerView;

    @Inject
    WishlistListAdapter wishlistListAdapter;
    @Inject
    IntentStarter intentStarter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_whishlistlist;
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(WishlistListComponent.class).inject(this);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(wishlistListAdapter);

        //dichiaro tutto questo nel fragment perchè devo poter avviare l'altra activity dal fragment
        wishlistListAdapter.setOnWishlistClickListener(new WishlistListAdapter.OnWishlistClickListener() {
            @Override
            public void onItemClick(View v , int position) {
                Timber.d("Clicked on wishlist! Id: "+ wishlistListAdapter.getItemId(position));
                //for now, all WL are empty, so I'll start productPicker
                //intentStarter.startWishlistActivity(getContext(), wishlistListAdapter.getItemId(position));
                intentStarter.startProductPickerSettingsActivity(getContext(), wishlistListAdapter.getItemId(position));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //contentView.setOnRefreshListener(this);*/
    }



    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public LceViewState<List<Wishlist>, WishlistListView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override public List<Wishlist> getData() {
        return wishlistListAdapter.getWishlistList();
    }

    @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        //return errorMessageDeterminer.getErrorMessage(e, pullToRefresh);
        return null;
    }

    @Override public WishlistListPresenter createPresenter() {
        return this.getComponent(WishlistListComponent.class).providePresenter();
    }

    @Override public void setData(List<Wishlist> data) {
        wishlistListAdapter.setWishlistList(data);
        wishlistListAdapter.notifyDataSetChanged();
    }

    @Override public void loadData(boolean pullToRefresh) {
        presenter.subscribe(pullToRefresh);
    }

    /*
    @Override public void onRefresh() {
        Random random = new Random();
        addWishlist(new Wishlist(random.nextLong(), "Wishlist"));
        loadData(true);
    }
    */

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        //contentView.setRefreshing(false);
        e.printStackTrace();
    }

    @Override public void showContent() {
        super.showContent();
        //contentView.setRefreshing(false);
    }

    @Override public void showLoading(boolean pullToRefresh) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Random random = new Random();
        switch (item.getItemId()) {
            case R.id.action_add:
                addWishlist(new Wishlist(Math.abs(random.nextLong()), "Wishlist"));
                return true;

            default:
                break;

        }
        return false;
    }

    @Override
    public void addWishlist(Wishlist wishlist) {
        eventBus.post(new WishlistAddedEvent(wishlist));
    }

    @Override
    public void removeWishlist(int wishlistId) {

    }

    @Override
    public void showWishlistAddedError() {

    }

    @Override
    public void showWishlistAddedSuccess() {
        ToastFactory.showShortToast(getContext(), R.string.wishlist_added);
    }
}

