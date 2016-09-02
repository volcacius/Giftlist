package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import timber.log.Timber;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistListAdapter extends SelectableAdapter<WishlistListAdapter.ViewHolder>
        implements SwipeableItemAdapter<WishlistListAdapter.ViewHolder>,
        DraggableItemAdapter<WishlistListAdapter.ViewHolder> {

    private static final int[] EMPTY_STATE = new int[] {};

    private final LayoutInflater layoutInflater;

    private List<Wishlist> wishlistList;
    private OnWishlistClickListener onWishlistClickListener;

    public WishlistListAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.wishlistList = new LinkedList<>();
        // DraggableItemAdapter and SwipeableItemAdapter require stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.list_wishlist, parent, false);
        ViewHolder vh = new ViewHolder(v, onWishlistClickListener); //needs the listener as a parameter
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String wishlistName = wishlistList.get(position).getName();
        holder.wishlistNameTextView.setText(wishlistName);
        holder.wishlistOccasionTextView.setText(wishlistList.get(position).getOccasion());

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
        }
        // set swiping properties
        holder.setSwipeItemHorizontalSlideAmount(0);

    }

    @Override
    public long getItemId(int position) {
        return wishlistList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return wishlistList.size();
    }

    public List<Wishlist> getSelectedWishlists() {
        List<Integer> positions = super.getSelectedItems();
        List<Wishlist> selectedWishlists = new ArrayList<>(positions.size());
        for (Integer i : positions) {
            selectedWishlists.add(wishlistList.get(i));
        }
        return selectedWishlists;
    }

    public List<Wishlist> getWishlistList() {
        return wishlistList;
    }

    public void setWishlistList(List<Wishlist> wishlistList) {
        this.wishlistList.clear();
        this.wishlistList.addAll(wishlistList);
    }

    //Called from fragment
    public void setOnWishlistClickListener(OnWishlistClickListener onWishlistClickListener) {
        this.onWishlistClickListener = onWishlistClickListener;
    }

    @Override
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return hitTest(dragHandleView, x - offsetX, y - offsetY);
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
        final Wishlist wishlist = wishlistList.remove(fromPosition);
        wishlistList.add(toPosition, wishlist);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        if (onCheckCanStartDrag(holder, position, x, y)) {
            return SwipeableItemConstants.REACTION_CAN_NOT_SWIPE_BOTH_H;
        } else {
            return SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H;
        }
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case SwipeableItemConstants.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }
        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, int result) {
        Timber.d("onSwipeItem(position = " + position + ", result = " + result + ")");
        switch (result) {
            // swipe right
            case SwipeableItemConstants.RESULT_SWIPED_RIGHT:
            // swipe left -- pin
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, position);
            // other --- do nothing
            case SwipeableItemConstants.RESULT_CANCELED:
            default:
                return null;
        }
    }

    class ViewHolder extends AbstractDraggableSwipeableItemViewHolder {

        @Bind(R.id.wishlist_name)
        TextView wishlistNameTextView;

        @Bind(R.id.wishlist_occasion)
        TextView wishlistOccasionTextView;

        @Bind(R.id.selected_overlay)
        View selectedOverlay;
        @Bind(R.id.container)
        public FrameLayout container;
        @Bind(R.id.drag_handle)
        public View dragHandle;

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
        private WishlistListAdapter mAdapter;
        private final int mPosition;
        private boolean mSetPinned;

        SwipeLeftResultAction(WishlistListAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    //This is a pattern to declare an onClick in the ViewHolder but implement it in the fragment
    //In Avengers: RecyclerClickListener
    public interface OnWishlistClickListener {
        public void onItemClick(View view , int position);
        public boolean onItemLongClick(View view, int position);
    }

    public static boolean hitTest(View v, int x, int y) {
        final int tx = (int) (ViewCompat.getTranslationX(v) + 0.5f);
        final int ty = (int) (ViewCompat.getTranslationY(v) + 0.5f);
        final int left = v.getLeft() + tx;
        final int right = v.getRight() + tx;
        final int top = v.getTop() + ty;
        final int bottom = v.getBottom() + ty;

        return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
    }
}
