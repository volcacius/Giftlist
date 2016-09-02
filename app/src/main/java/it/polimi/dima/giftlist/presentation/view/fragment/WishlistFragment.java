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

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @Arg
    long wishlistId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionModeCallback = new ActionModeCallback();
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
        Timber.d("wl fragment onViewCreated");

        //to avoid bugs. it would be better to retain selected instances but this is a good enough tradeoff
        if (actionMode!=null) {
            actionMode.finish();
        }

        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(wishlistAdapter);

        wishlistAdapter.setOnProductClickListener(new WishlistAdapter.OnProductClickListener() {@Override
        public void onItemClick(View v , int position) {
            if (actionMode != null) {
                toggleSelection(position);
            } else {
                intentStarter.startProductDetailsPagerActivity(getContext(), wishlistAdapter.getProductList(), wishlistAdapter.getItem(position).getId());
            }
        }

            @Override
            public boolean onItemLongClick(View view, int position) {
                Timber.d("fragment long click");

                if (actionMode == null) {
                    Timber.d("I'm not in action mode");
                    actionMode =((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                } else {
                    Timber.d("I'm in action mode");
                }
                toggleSelection(position);

                return true;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void toggleSelection(int position) {
        wishlistAdapter.toggleSelection(position);
        int count = wishlistAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
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
        Timber.d("wl fragment setData");
        wishlistAdapter.setProductList(data);
        wishlistAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Timber.d("wl fragment loadData");
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
        getPresenter().removeProduct(product);
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.delete_item_context_menu, menu);
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
                    for (Product p : wishlistAdapter.getSelectedWishlists()) {
                        removeProduct(p);
                    }
                    wishlistAdapter.notifyDataSetChanged();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            Timber.d("destroying action mode");
            wishlistAdapter.clearSelection();
            actionMode = null;
            Timber.d("now I have " + wishlistAdapter.getSelectedWishlists().size());
        }
    }


}
