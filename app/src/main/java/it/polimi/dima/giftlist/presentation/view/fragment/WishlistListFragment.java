package it.polimi.dima.giftlist.presentation.view.fragment;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistListPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistListAdapter;
import it.polimi.dima.giftlist.util.ViewUtil;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListFragment extends BaseMvpLceFragment<RecyclerView, List<Wishlist>, WishlistListView, WishlistListPresenter>
        implements WishlistListView {

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

    @OnClick(R.id.fab)
    void onFabClick() {
        IntentStarter.startWishlistSettingsActivity(getContext(), Wishlist.DEFAULT_ID, wishlistListAdapter.getWishlistList().size());
    }

    @Inject
    WishlistListAdapter wishlistListAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionModeCallback = new ActionModeCallback();
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
        if (actionMode != null) {
            actionMode.finish();
        }
        super.onViewCreated(view, savedInstanceState);

        //Set collapsing bar as activity's actionbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        initCollapsingToolbar();

        wishlistListAdapter.setWishlistListView(this);
        wishlistListAdapter.setOnWishlistClickListener(new WishlistListAdapter.OnWishlistClickListener() {
            @Override
            public void onItemClick(View v , int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                } else {
                    IntentStarter.startWishlistActivity(getContext(), wishlistListAdapter.getItemId(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                if (actionMode == null) {
                    actionMode =((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                } else {
                    //null
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
        wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(wishlistListAdapter);
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
                " ",
                Snackbar.LENGTH_LONG);

        undoDeleteSnackBar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUndoDeleteWishlists();
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
        if (wishlistListAdapter != null) {
            wishlistListAdapter = null;
        }
        if (recyclerView != null) {
            recyclerView = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_wishlistlist, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LinkedList<Wishlist> filteredModelList = Wishlist.filter(wishlistListAdapter.getWishlistList(), newText);
                wishlistListAdapter.setFilterableWishlistList(filteredModelList);
                recyclerView.scrollToPosition(0);
                wishlistListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void backlogDeletionDBCleanup() {
        //Perform any backlog deletion
        removeWishlistsFromDBByIds(wishlistListAdapter.getWishlistsIdsToDelete());
        //Update display order in db
        updateWishlistsDisplayOrderInDB();
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
        wishlistListAdapter.toggleSelection(position);
        int count = wishlistListAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        //ErrorMessageDeterminer.getErrorMessage(e, pullToRefresh);
        return null;
    }

    @Override public WishlistListPresenter createPresenter() {
        return this.getComponent(WishlistListComponent.class).providePresenter();
    }

    @DebugLog
    @Override
    public void setData(List<Wishlist> data) {
        wishlistListAdapter.setWishlistList(data);
    }

    @Override
    @DebugLog
    public void loadData(boolean pullToRefresh) {
        presenter.subscribe(pullToRefresh);
    }

    @Override
    @DebugLog
    public void removeWishlistsFromDBByIds(List<Long> wishlistIds) {
        for (long id : wishlistIds) {
            getPresenter().removeWishlist(id);
        }
    }

    @Override
    @DebugLog
    public void onWishlistsRemovedFromView() {
        String message = wishlistListAdapter.getWishlistsIdsToDelete().size() + " wishlists deleted";
        undoDeleteSnackBar.setText(message);
        undoDeleteSnackBar.show();
    }

    private void onUndoDeleteWishlists() {
        //In order to undo the delete, clear the list of wishlists to delete and reload the data from the DB
        wishlistListAdapter.clearWishlistsIdsToDelete();
        //clear search, if any
        clearSearchView();
        loadData(false);
    }

    @Override
    public void clearSearchView() {
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    public void updateWishlistsDisplayOrderInDB() {
        //store the order of the wishlists
        presenter.updateWishlistListOrder(wishlistListAdapter.getWishlistList());
    }

    @Override
    public boolean isUndoBarVisible() {
        return undoDeleteSnackBar.isShownOrQueued();
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.delete_item_context_menu, menu);
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
                    //First get the list of ids to remove based on selection
                    List<Long> wishlistsIdsToRemove = wishlistListAdapter.getSelectedWishlistsIds();
                    //Perform deletion on the backlog
                    removeWishlistsFromDBByIds(wishlistListAdapter.getWishlistsIdsToDelete());
                    //set the current selected set as to delete
                    wishlistListAdapter.updateWishlistsIdsToDelete(wishlistsIdsToRemove);
                    //Then remove from view the current selected set
                    wishlistListAdapter.removeSelectedFilteredWishlistsFromView(wishlistsIdsToRemove);
                    mode.finish();
                    //Show undo message
                    onWishlistsRemovedFromView();
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

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
        picasso.load(R.drawable.party)
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
                    collapsingToolbar.setTitle(getString(R.string.your_wishlists));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}

