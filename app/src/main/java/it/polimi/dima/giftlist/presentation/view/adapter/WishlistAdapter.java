package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Product;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistAdapter extends SelectableAdapter<WishlistAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    private List<Product> productList;
    private OnProductClickListener onProductClickListener;

    public WishlistAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.list_product, parent, false);
        ViewHolder vh = new ViewHolder(v, onProductClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String productName = productList.get(position).getName();
        holder.productNameTextView.setText(productName);

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    public Product getItem(int position) {
        return productList.get(position);
    }

    public List<Product> getSelectedWishlists() {
        List<Integer> positions = super.getSelectedItems();
        List<Product> selectedWishlists = new ArrayList<>(positions.size());
        for (Integer i : positions) {
            selectedWishlists.add(productList.get(i));
        }
        return selectedWishlists;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setOnProductClickListener(OnProductClickListener onProductClickListener) {
        this.onProductClickListener = onProductClickListener;
    }

    //This is a pattern to declare an onClick in the ViewHolder but implement it in the fragment
    public interface OnProductClickListener {
        public void onItemClick(View view , int position);

        public boolean onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.wishlist_product_name)
        public TextView productNameTextView;

        @Bind(R.id.selected_overlay)
        View selectedOverlay;

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
        }
    }
}
