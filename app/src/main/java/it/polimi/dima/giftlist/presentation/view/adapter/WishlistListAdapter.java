package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import it.polimi.dima.giftlist.util.ViewUtil;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistListAdapter extends SelectableAdapter<WishlistListAdapter.ViewHolder>
        implements SwipeableItemAdapter<WishlistListAdapter.ViewHolder>,
        DraggableItemAdapter<WishlistListAdapter.ViewHolder> {

    Picasso picasso;

    private static final int[] EMPTY_STATE = new int[] {};

    private Context context;
    private final LayoutInflater layoutInflater;
    private List<Wishlist> wishlistList;
    private OnWishlistClickListener onWishlistClickListener;
    private LinkedList<Wishlist> filterableWishlistList;
    private List<Long> wishlistsIdsToDelete;
    private WishlistListView wishlistListView;

    public WishlistListAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.wishlistList = new LinkedList<>();
        this.filterableWishlistList = new LinkedList<>();
        this.wishlistsIdsToDelete = new ArrayList<>();
        // DraggableItemAdapter and SwipeableItemAdapter require stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return filterableWishlistList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return filterableWishlistList.size();
    }

    @DebugLog
    public List<Long> getSelectedWishlistsIds() {
        List<Integer> positions = super.getSelectedItems();
        List<Long> selectedWishlistsIds = new ArrayList<>(positions.size());
        for (Integer i : positions) {
            selectedWishlistsIds.add(filterableWishlistList.get(i).getId());
        }
        return selectedWishlistsIds;
    }

    @DebugLog
    public void removeSelectedFilteredWishlistsFromView(List<Long> selectedFilteredWishlistIds) {
        //Remove the wishlists from original and filtered list
        for (long i : selectedFilteredWishlistIds) {
            for (Wishlist wf : filterableWishlistList) {
                if (i == wf.getId()) {
                    filterableWishlistList.remove(wf);
                    break;
                }
            }
            for (Wishlist wf : wishlistList) {
                if (i == wf.getId()) {
                    wishlistList.remove(wf);
                    break;
                }
            }
        }
        notifyDataSetChanged();
        //Update the display order for both lists
        updateDisplayOrder(filterableWishlistList);
        updateDisplayOrder(wishlistList);
    }

    public List<Wishlist> getWishlistList() {
        return wishlistList;
    }

    public LinkedList<Wishlist> getFilterableWishlistList() {
        return filterableWishlistList;
    }

    @DebugLog
    public void setWishlistList(List<Wishlist> wishlistList) {
        this.wishlistList.clear();
        this.filterableWishlistList.clear();
        this.wishlistList.addAll(wishlistList);
        this.filterableWishlistList.addAll(wishlistList);
        notifyDataSetChanged();
    }

    //Called from fragment
    public void setOnWishlistClickListener(OnWishlistClickListener onWishlistClickListener) {
        this.onWishlistClickListener = onWishlistClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.list_wishlist, parent, false);
        ViewHolder vh = new ViewHolder(v, onWishlistClickListener); //needs the listener as a parameter
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String wishlistName = filterableWishlistList.get(position).getName();
        String occasion = filterableWishlistList.get(position).getOccasion();
        int order = filterableWishlistList.get(position).getDisplayOrder();

        holder.wishlistNameTextView.setText(String.format("%d %s", order, wishlistName));
        holder.wishlistOccasionTextView.setText(occasion);

        picasso.load(getWishlistThumbnail(occasion))
                .fit()
                .centerCrop()
                .into(holder.thumbnail);

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & DraggableItemConstants.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & SwipeableItemConstants.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            if ((dragState & DraggableItemConstants.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;
                // need to clear drawable state here to get correct appearance of the dragging item.
                Drawable drawable = holder.container.getForeground();
                if (drawable != null) {
                    drawable.setState(EMPTY_STATE);
                }
            } else if ((dragState & DraggableItemConstants.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else if ((swipeState & SwipeableItemConstants.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & SwipeableItemConstants.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }
            holder.container.setBackgroundResource(bgResId);
            holder.overflowIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v, filterableWishlistList.get(position).getId());
                }
            });
        }
        holder.setSwipeItemHorizontalSlideAmount(0);
    }

    private int getWishlistThumbnail(String occasion) {
        if (occasion.equals(context.getString(R.string.birthday))) {
            return R.drawable.birthday;
        } else if (occasion.equals(context.getString(R.string.anniversary))) {
            return R.drawable.cake_anniversary;
        } else if (occasion.equals(context.getString(R.string.graduation))) {
            return R.drawable.beer;
        } else if (occasion.equals(context.getString(R.string.wedding))) {
            return R.drawable.wife;
        } else {
            return R.drawable.lights;
        }
    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        //Can't start dragging if it is selecting
        if (wishlistListView.isSelectModeEnabled()) {
            return false;
        }

        //Can't start dragging if it is filtering
        if (filterableWishlistList.size() != wishlistList.size()) {
            return false;
        }
        //can't start dragging if the view is showing undo delete
        if (wishlistListView.isUndoBarVisible()) {
            return false;
        }
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;
        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);
        return ViewUtil.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(ViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        //reorder sorted list
        Wishlist filterableWishlist = filterableWishlistList.remove(fromPosition);
        filterableWishlistList.add(toPosition, filterableWishlist);
        updateDisplayOrder(filterableWishlistList);
        //reorder original wishlistlist
        Wishlist wishlist = wishlistList.remove(fromPosition);
        wishlistList.add(toPosition, wishlist);
        updateDisplayOrder(wishlistList);
        notifyItemMoved(fromPosition, toPosition);
        //Persist result to the DB
        //Safe to do since can't move when a undo bar is showing
        wishlistListView.backlogDeletionDBCleanup();
    }

    private void updateDisplayOrder(List<Wishlist> wishlistList) {
        for (int order = wishlistList.size() - 1; order >= 0; order--) {
            wishlistList.get(wishlistList.size() - 1 - order).setDisplayOrder(order);
        }
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        if (!wishlistListView.isSelectModeEnabled()) {
            return SwipeableItemConstants.REACTION_CAN_SWIPE_LEFT;
        } else {
            return SwipeableItemConstants.REACTION_CAN_NOT_SWIPE_ANY;
        }
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
            case SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
            default:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
        }
        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, int result) {
        switch (result) {
            // swipe right
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, wishlistListView, position);
            // other --- do nothing
            case SwipeableItemConstants.RESULT_SWIPED_RIGHT:
            case SwipeableItemConstants.RESULT_CANCELED:
            default:
                return null;
        }
    }

    public void setFilterableWishlistList(LinkedList<Wishlist> filterableWishlistList) {
        this.filterableWishlistList = filterableWishlistList;
    }

    @DebugLog
    public List<Long> getWishlistsIdsToDelete() {
        return wishlistsIdsToDelete;
    }

    @DebugLog
    public void updateWishlistsIdsToDelete(List<Long> newWishlistsIdsToDelete) {
        //Clear the list of IDs (should be already done when performing backlog deletion)
        clearWishlistsIdsToDelete();
        //Add the new list of IDs to delete
        wishlistsIdsToDelete.addAll(newWishlistsIdsToDelete);
    }

    @DebugLog
    public void clearWishlistsIdsToDelete() {
        wishlistsIdsToDelete.clear();
    }

    public void setWishlistListView(WishlistListView wishlistListView) {
        this.wishlistListView = wishlistListView;
    }

    class ViewHolder extends AbstractDraggableSwipeableItemViewHolder {

        @Bind(R.id.wishlist_name)
        TextView wishlistNameTextView;
        @Bind(R.id.occasion)
        TextView wishlistOccasionTextView;
        @Bind(R.id.selected_overlay)
        View selectedOverlay;
        @Bind(R.id.container)
        FrameLayout container;
        @Bind(R.id.drag_handle)
        View dragHandle;
        @Bind(R.id.overflow)
        ImageView overflowIcon;
        @Bind(R.id.thumbnail)
        ImageView thumbnail;

        public ViewHolder(View view, OnWishlistClickListener onWishlistClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            bindListener(view, onWishlistClickListener);
        }

        //method to bind the listener
        private void bindListener(View view, OnWishlistClickListener onWishlistClickListener) {
            view.setOnClickListener(v ->
            onWishlistClickListener.onItemClick(v, getPosition()));
            view.setOnLongClickListener(v ->
                    onWishlistClickListener.onItemLongClick(v, getPosition()));
            container.setOnClickListener(v ->
                    onWishlistClickListener.onItemClick(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), getPosition()));
            container.setOnLongClickListener(v ->
                    onWishlistClickListener.onItemLongClick(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), getPosition()));
        }

        @Override
        public View getSwipeableContainerView() {
            return container;
        }
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private WishlistListAdapter adapter;
        private final int position;
        private WishlistListView wishlistListView;

        SwipeLeftResultAction(WishlistListAdapter adapter, WishlistListView wishlistListView, int position) {
            this.adapter = adapter;
            this.position = position;
            this.wishlistListView = wishlistListView;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            //Get wishlist id to remove
            long wishlistIdToRemove = adapter.filterableWishlistList.get(position).getId();
            //Perform backlog deletion
            wishlistListView.removeWishlistsFromDBByIds(adapter.getWishlistsIdsToDelete());
            adapter.clearWishlistsIdsToDelete();
            //Set the removed list as to be deleted
            List<Long> wishlistIdToRemoveAsList = new ArrayList<>();
            wishlistIdToRemoveAsList.add(wishlistIdToRemove);
            adapter.updateWishlistsIdsToDelete(wishlistIdToRemoveAsList);
            //Remove swiped wishlist from view
            adapter.removeSelectedFilteredWishlistsFromView(wishlistIdToRemoveAsList);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            //Show the undo button after removal from view
            wishlistListView.onWishlistsRemovedFromView();
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            adapter = null;
        }
    }

    //This is a pattern to declare an onClick in the ViewHolder but implement it in the fragment
    //In Avengers: RecyclerClickListener
    public interface OnWishlistClickListener {
        public void onItemClick(View view , int position);
        public boolean onItemLongClick(View view, int position);
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, long wishlistId) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_wishlist, popup.getMenu());
        popup.setOnMenuItemClickListener(new WishlistPopupClickListener(wishlistId));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class WishlistPopupClickListener implements PopupMenu.OnMenuItemClickListener {

        long wishlistId;

        public WishlistPopupClickListener(long wishlistId) {
            this.wishlistId = wishlistId;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add:
                    IntentStarter.startProductPickerSettingsActivity(context, wishlistId);
                    return true;
                case R.id.action_settings:
                    //I'm passing the default order value here since it is an update of the wishlist, so it won't be used
                    IntentStarter.startWishlistSettingsActivity(context, wishlistId, Wishlist.DEFAULT_ORDER);
                    return true;
                default:
                    break;
            }
            return false;
        }
    }
}
