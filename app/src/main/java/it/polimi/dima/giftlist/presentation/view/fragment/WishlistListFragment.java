package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
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
public class WishlistListFragment extends BaseMvpLceFragment<RecyclerView, List<Wishlist>, WishlistListView, WishlistListPresenter>
        implements WishlistListView {

    @Bind(R.id.contentView)
    RecyclerView recyclerView;

    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

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

        wishlistListAdapter.setOnWishlistClickListener(new WishlistListAdapter.OnWishlistClickListener() {
            @Override
            public void onItemClick(View v , int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                } else {
                    intentStarter.startWishlistActivity(getContext(), wishlistListAdapter.getItemId(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                Timber.d("fragment long click");
                if (actionMode == null) {
                    actionMode =((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                }

                toggleSelection(position);

                return true;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void toggleSelection(int position) {
        wishlistListAdapter.toggleSelection(position);
        int count = wishlistListAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                intentStarter.startWishlistSettingsActivity(getContext(), 0);
                return true;

            default:
                break;

        }
        return false;
    }

    @Override
    public void removeWishlist(long wishlistId) {
        getPresenter().removeWishlist(wishlistId);
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.wishlist_list_item_context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    Timber.d("remove action");
                    for (Wishlist w : wishlistListAdapter.getSelectedWishlists()) {
                        removeWishlist(w.getId());
                    }
                    wishlistListAdapter.notifyDataSetChanged();
                    //wishlistListAdapter.removeItems(adapter.getSelectedItems());
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            wishlistListAdapter.clearSelection();
            actionMode = null;
        }
    }
}

