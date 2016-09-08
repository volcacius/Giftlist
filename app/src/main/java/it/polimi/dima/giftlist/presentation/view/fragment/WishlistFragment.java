package it.polimi.dima.giftlist.presentation.view.fragment;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import icepick.State;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistView;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistAdapter;
import it.polimi.dima.giftlist.util.ViewUtil;

/**
 * Created by Alessandro on 24/04/16.
 */
@FragmentWithArgs
public class WishlistFragment extends BaseMvpLceFragment<RecyclerView, List<Product>, WishlistView, WishlistPresenter>
        implements WishlistView {

    @Bind(R.id.contentView)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.backdrop)
    ImageView collapseBackdrop;
    @Bind(R.id.main_content)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.wishlist_header_title)
    TextView wishlistHeaderTitle;
    @Bind(R.id.wishlist_header_subtitle)
    TextView wishlistHeaderSubtitle;

    @OnClick(R.id.fab)
    void onFabClick() {
        //I can pass the default order even though it's not the correct one since it's just an update of the wishlist
        IntentStarter.startWishlistSettingsActivity(getContext(), wishlistId, Wishlist.DEFAULT_ORDER);
    }

    @Inject
    WishlistAdapter wishlistAdapter;
    @Inject
    Picasso picasso;

    RecyclerViewDragDropManager recyclerViewDragDropManager;
    RecyclerViewSwipeManager recyclerViewSwipeManager;
    RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    RecyclerView.Adapter wrappedAdapter;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Snackbar undoDeleteSnackBar;
    private SearchView searchView;

    @Arg
    @State
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
        if (actionMode != null) {
            actionMode.finish();
        }

        super.onViewCreated(view, savedInstanceState);
        //Set collapsing bar as activity's actionbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        presenter.setActionBarDetails(wishlistId);
        wishlistAdapter.setWishlistView(this);
        wishlistAdapter.setOnProductClickListener(new WishlistAdapter.OnProductClickListener() {
            @Override
            public void onItemClick(View v , int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                } else {
                    IntentStarter.startProductDetailsPagerActivity(getContext(), wishlistAdapter.getProductList(), wishlistAdapter.getItem(position).getId());
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                if (actionMode == null) {
                    actionMode =((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                } else {
                }
                toggleSelection(position);
                return true;
            }
        });

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);
        // drag & drop manager
        recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        recyclerViewDragDropManager.setDraggingItemShadowDrawable((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));
        // swipe manager
        recyclerViewSwipeManager = new RecyclerViewSwipeManager();
        // wrap for dragging
        wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(wishlistAdapter);
        // wrap for swiping
        wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(wrappedAdapter);

        GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);
        recyclerView.setItemAnimator(animator);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // requires *wrapped* adapter
        recyclerView.setAdapter(wrappedAdapter);
        //For smooth image loading
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheEnabled(true);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (ViewUtil.supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        // priority: TouchActionGuard > Swipe > DragAndDrop
        recyclerViewTouchActionGuardManager.attachRecyclerView(recyclerView);
        recyclerViewSwipeManager.attachRecyclerView(recyclerView);
        recyclerViewDragDropManager.attachRecyclerView(recyclerView);

        //Setup undo delete snackbar
        undoDeleteSnackBar = Snackbar.make(
                //Set the coordinator layout as parent to enable coordination with FAB
                coordinatorLayout,
                //set the displayed string here to empty, will be customized on show
                "",
                Snackbar.LENGTH_LONG);

        undoDeleteSnackBar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUndoDeleteProducts();
            }
        });
        undoDeleteSnackBar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.snackbar_action_color_done));
        undoDeleteSnackBar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                backlogDeletionDBCleanup();
            }
        });
    }

    @Override
    public void onPause() {
        recyclerViewDragDropManager.cancelDrag();
        super.onPause();
        backlogDeletionDBCleanup();
        clearSearchView();
        if (undoDeleteSnackBar.isShownOrQueued()) {
            undoDeleteSnackBar.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.release();
            recyclerViewDragDropManager = null;
        }
        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager.release();
            recyclerViewSwipeManager = null;
        }
        if (recyclerViewTouchActionGuardManager != null) {
            recyclerViewTouchActionGuardManager.release();
            recyclerViewTouchActionGuardManager = null;
        }
        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        if (wishlistAdapter != null) {
            wishlistAdapter = null;
        }
        if (recyclerView != null) {
            recyclerView = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu wishlistlist is the same for here
        getActivity().getMenuInflater().inflate(R.menu.menu_wishlist, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LinkedList<Product> filteredModelList = Product.filter(wishlistAdapter.getProductList(), newText);
                wishlistAdapter.setFilterableProductList(filteredModelList);
                recyclerView.scrollToPosition(0);
                wishlistAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void backlogDeletionDBCleanup() {
        //Perform any backlog deletion
        removeProductsFromDB(wishlistAdapter.getProductsToDelete());
        //Update display order in db
        updateProductsDisplayOrderInDB();
    }

    public void updateProductsDisplayOrderInDB() {
        //store the order of the wishlists
        presenter.updateProductListOrder(wishlistAdapter.getProductList());
    }

    @Override
    public boolean isSelectModeEnabled() {
        if (actionMode != null) {
            return true;
        } else {
            return false;
        }
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
    public boolean isUndoBarVisible() {
        return undoDeleteSnackBar.isShownOrQueued();
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
        wishlistAdapter.setProductList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.subscribe(pullToRefresh);
    }

    public void removeProductsFromDB(List<Product> productList) {
        for (Product p : productList) {
            getPresenter().removeProduct(p);
        }
    }

    @Override
    public void onProductsRemovedFromView() {
        String message = wishlistAdapter.getProductsToDelete().size() + " products deleted";
        undoDeleteSnackBar.setText(message);
        undoDeleteSnackBar.show();
    }

    private void onUndoDeleteProducts() {
        //In order to undo the delete, clear the list of wishlists to delete and reload the data from the DB
        wishlistAdapter.clearProductsToDelete();
        //clear search, if any
        clearSearchView();
        loadData(false);
    }

    @Override
    public void clearSearchView() {
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //I'm passing the default order value here since it is an update of the wishlist, so it won't be used
                IntentStarter.startWishlistSettingsActivity(getContext(), wishlistId, Wishlist.DEFAULT_ORDER);
                return true;
            case R.id.action_home:
                //I'm passing the default order value here since it is an update of the wishlist, so it won't be used
                IntentStarter.startWishlistListActivity(getContext());
                return true;
            default:
                break;
        }
        return false;
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
                    //First get the list of products based on selection
                    List<Product> productsToRemove = wishlistAdapter.getSelectedProducts();
                    //First get the list of ids to remove based on selection
                    List<Long> productsIdsToRemove = wishlistAdapter.getProductsIds(productsToRemove);
                    //Perform deletion on the backlog
                    removeProductsFromDB(wishlistAdapter.getProductsToDelete());
                    //set the current selected set as to delete
                    wishlistAdapter.updateProductsToDelete(productsToRemove);
                    //Then remove from view the current selected set
                    wishlistAdapter.removeSelectedFilteredProductsFromView(productsIdsToRemove);
                    mode.finish();
                    //Show undo message
                    onProductsRemovedFromView();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            wishlistAdapter.clearSelection();
            actionMode = null;
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    @Override
    public void initCollapsingToolbar(String name, String occasion) {
        wishlistHeaderTitle.setText(name);
        wishlistHeaderSubtitle.setText(occasion);

        collapsingToolbar.setTitle(" ");
        picasso.load(Wishlist.getWishlistThumbnail(getContext(), occasion))
                .fit()
                .centerCrop()
                .into(collapseBackdrop);

        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(String.format("%s' %s", name, occasion));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
