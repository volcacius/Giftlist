package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.WishlistView;
import it.polimi.dima.giftlist.util.ViewUtil;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistAdapter extends SelectableAdapter<WishlistAdapter.ViewHolder> 
    implements SwipeableItemAdapter<WishlistAdapter.ViewHolder>,
        DraggableItemAdapter<WishlistAdapter.ViewHolder> {

    private static final int[] EMPTY_STATE = new int[] {};

    private Context context;
    private Picasso picasso;
    private LayoutInflater layoutInflater;
    private List<Product> productList;
    private LinkedList<Product> filterableProductList;
    private List<Product> productsToDelete;
    private WishlistView wishlistView;
    private OnProductClickListener onProductClickListener;

    public WishlistAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productList = new LinkedList<>();
        this.filterableProductList = new LinkedList<>();
        this.productsToDelete = new ArrayList<>();
        // DraggableItemAdapter and SwipeableItemAdapter require stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    public List<Product> getProductsToDelete() {
        return productsToDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.list_product, parent, false);
        ViewHolder vh = new ViewHolder(v, onProductClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product currentProduct = productList.get(position);
        String productName = currentProduct.getName();
        String imageUri = currentProduct.getImageUri();
        float price = currentProduct.getPrice();
        float convertedPrice = currentProduct.getConvertedPrice();
        CurrencyType currencyType = currentProduct.getCurrencyType();

        holder.productNameTextView.setText(productName);
        holder.priceTextView.setText(String.format("%.2f %s", price, currencyType.toString()));
        if (convertedPrice != 0 && !currencyType.equals(CurrencyType.EUR)) {
            holder.convertedPriceTextView.setText(String.format("(%.2f %s)", convertedPrice, CurrencyType.EUR.toString()));
        }

        if (imageUri == null) {
            ColorDrawable colorDrawable = new ColorDrawable(holder.colorPrimary);
            holder.thumbnail.setDrawingCacheEnabled(true);
            holder.thumbnail.setImageDrawable(colorDrawable);
        } else {
            File thumbnail = new File(imageUri);
            picasso.load(thumbnail)
                    .fit()
                    .centerCrop()
                    .into(holder.thumbnail);
        }
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
        holder.setSwipeItemHorizontalSlideAmount(0);
    }

    @Override
    public long getItemId(int position) {
        return filterableProductList.get(position).getId();
    }

    public Product getItem(int position) {
        return filterableProductList.get(position);
    }

    public List<Long> getProductsIds(List<Product> productList) {
        List<Long> productsIds = new ArrayList<>();
        for (Product p : productList) {
            productsIds.add(p.getId());
        }
        return productsIds;
    }

    public List<Product> getSelectedProducts() {
        List<Integer> positions = super.getSelectedItems();
        List<Product> selectedProducts = new ArrayList<>(positions.size());
        for (Integer i : positions) {
            selectedProducts.add(filterableProductList.get(i));
        }
        return selectedProducts;
    }

    @DebugLog
    public void removeSelectedFilteredProductsFromView(List<Long> selectedFilteredProductIds) {
        //Remove the products from original and filtered list
        for (long i : selectedFilteredProductIds) {
            for (Product fp : filterableProductList) {
                if (i == fp.getId()) {
                    filterableProductList.remove(fp);
                    break;
                }
            }
            for (Product p : productList) {
                if (i == p.getId()) {
                    productList.remove(p);
                    break;
                }
            }
        }
        notifyDataSetChanged();
        //Update the display order for both lists
        updateDisplayOrder(filterableProductList);
        updateDisplayOrder(productList);
    }

    @Override
    public int getItemCount() {
        return filterableProductList.size();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList.clear();
        this.filterableProductList.clear();
        this.productList.addAll(productList);
        this.filterableProductList.addAll(productList);
        notifyDataSetChanged();
    }

    public void setOnProductClickListener(OnProductClickListener onProductClickListener) {
        this.onProductClickListener = onProductClickListener;
    }

    public void setWishlistView(WishlistView wishlistView) {
        this.wishlistView = wishlistView;
    }

    public void setFilterableProductList(LinkedList<Product> filterableProductList) {
        this.filterableProductList = filterableProductList;
    }

    //This is a pattern to declare an onClick in the ViewHolder but implement it in the fragment
    public interface OnProductClickListener {
        public void onItemClick(View view , int position);

        public boolean onItemLongClick(View view, int position);
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        if (!wishlistView.isSelectModeEnabled()) {
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
    public boolean onCheckCanStartDrag(ViewHolder holder, int position, int x, int y) {
        //Can't start dragging if it is selecting
        if (wishlistView.isSelectModeEnabled()) {
            return false;
        }

        //Can't start dragging if it is filtering
        if (filterableProductList.size() != productList.size()) {
            return false;
        }
        //can't start dragging if the view is showing undo delete
        if (wishlistView.isUndoBarVisible()) {
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
        Product filterableProduct = filterableProductList.remove(fromPosition);
        filterableProductList.add(toPosition, filterableProduct);
        updateDisplayOrder(filterableProductList);
        //reorder original productlist
        Product product = productList.remove(fromPosition);
        productList.add(toPosition, product);
        updateDisplayOrder(productList);
        notifyItemMoved(fromPosition, toPosition);
        //Persist result to the DB
        //Safe to do since can't move when a undo bar is showing
        wishlistView.backlogDeletionDBCleanup();
    }

    private void updateDisplayOrder(List<Product> productList) {
        for (int order = productList.size(); order > 0; order--) {
            //I want the display order to go from size to 1
            productList.get(productList.size() - order).setDisplayOrder(order);
        }
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, int result) {
        switch (result) {
            // swipe right
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, wishlistView, position);
            // other --- do nothing
            case SwipeableItemConstants.RESULT_SWIPED_RIGHT:
            case SwipeableItemConstants.RESULT_CANCELED:
            default:
                return null;
        }
    }

    @DebugLog
    public void updateProductsToDelete(List<Product> newProductsToDelete) {
        //Clear the list of IDs (should be already done when performing backlog deletion)
        clearProductsToDelete();
        //Add the new list of IDs to delete
        productsToDelete.addAll(newProductsToDelete);
    }

    @DebugLog
    public void clearProductsToDelete() {productsToDelete.clear();
    }

    public class ViewHolder extends AbstractDraggableSwipeableItemViewHolder{

        @Bind(R.id.product_name)
        public TextView productNameTextView;
        @Bind(R.id.selected_overlay)
        View selectedOverlay;
        @Bind(R.id.container)
        FrameLayout container;
        @Bind(R.id.drag_handle)
        View dragHandle;
        @Bind(R.id.thumbnail)
        ImageView thumbnail;
        @Bind(R.id.product_price_converted)
        TextView convertedPriceTextView;
        @BindColor(R.color.colorPrimary)
        public int colorPrimary;
        @Bind(R.id.product_price)
        public TextView priceTextView;

        public ViewHolder(View view, OnProductClickListener onProductClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            bindListener(view, onProductClickListener);
        }

        //method to bind the listener
        private void bindListener(View view, OnProductClickListener onProductClickListener) {
            view.setOnClickListener(v ->
                    onProductClickListener.onItemClick(v, getPosition()));

            view.setOnLongClickListener(v ->
                    onProductClickListener.onItemLongClick(v, getPosition()));
            container.setOnClickListener(v ->
                    onProductClickListener.onItemClick(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), getPosition()));
            container.setOnLongClickListener(v ->
                    onProductClickListener.onItemLongClick(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), getPosition()));
        }

        @Override
        public View getSwipeableContainerView() {
            return container;
        }
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private WishlistAdapter adapter;
        private final int position;
        private WishlistView wishlistView;

        SwipeLeftResultAction(WishlistAdapter adapter, WishlistView wishlistView, int position) {
            this.adapter = adapter;
            this.position = position;
            this.wishlistView = wishlistView;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            //Get product to remove
            Product productToRemove = adapter.filterableProductList.get(position);
            //Perform backlog deletion
            wishlistView.removeProductsFromDB(adapter.getProductsToDelete());
            adapter.clearProductsToDelete();
            //Set the removed product as to be deleted
            List<Product> productToRemoveAsList = new ArrayList<>();
            productToRemoveAsList.add(productToRemove);
            adapter.updateProductsToDelete(productToRemoveAsList);
            //Remove swiped product from view
            List<Long> productIdToRemoveAsList = new ArrayList<>();
            productIdToRemoveAsList.add(productToRemove.getId());
            adapter.removeSelectedFilteredProductsFromView(productIdToRemoveAsList);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            //Show the undo button after removal from view
            wishlistView.onProductsRemovedFromView();
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            adapter = null;
        }
    }
}
