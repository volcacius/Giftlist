package it.polimi.dima.giftlist.presentation.view.fragment;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistListPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import it.polimi.dima.giftlist.presentation.view.adapter.WishlistListAdapter;
import timber.log.Timber;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListFragment extends BaseMvpLceFragment<RecyclerView, List<Wishlist>, WishlistListView, WishlistListPresenter>
        implements WishlistListView {

    @Bind(R.id.contentView)
    RecyclerView recyclerView;

    @Inject
    WishlistListAdapter wishlistListAdapter;
    @Inject
    IntentStarter intentStarter;

    RecyclerViewDragDropManager recyclerViewDragDropManager;
    RecyclerViewSwipeManager recyclerViewSwipeManager;
    RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    RecyclerView.Adapter wrappedAdapter;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.d("wlList fragment onCreated");
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
        Timber.d("wlList fragment onViewCreated");
        //to avoid bugs. it would be better to retain selected instances but this is a good enough tradeoff
        if (actionMode!=null) {
            actionMode.finish();
        }
        super.onViewCreated(view, savedInstanceState);

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

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);

        // drag & drop manager
        recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        recyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));

        // swipe manager
        recyclerViewSwipeManager = new RecyclerViewSwipeManager();

        wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(wishlistListAdapter);      // wrap for dragging
        wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(wrappedAdapter);      // wrap for swiping

        GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(wrappedAdapter);  // requires *wrapped* adapter
        recyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        recyclerViewTouchActionGuardManager.attachRecyclerView(recyclerView);
        recyclerViewSwipeManager.attachRecyclerView(recyclerView);
        recyclerViewDragDropManager.attachRecyclerView(recyclerView);
    }


    @Override
    public void onPause() {
        recyclerViewDragDropManager.cancelDrag();
        super.onPause();
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

        super.onDestroyView();
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
                    for (Wishlist w : wishlistListAdapter.getSelectedWishlists()) {
                        removeWishlist(w.getId());
                    }
                    wishlistListAdapter.notifyDataSetChanged();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            Timber.d("destroying action mode");
            wishlistListAdapter.clearSelection();
            actionMode = null;
            Timber.d("now I have " + wishlistListAdapter.getSelectedWishlists().size());
        }
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }
}

