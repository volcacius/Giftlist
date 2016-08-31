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
import it.polimi.dima.giftlist.data.model.Wishlist;
import timber.log.Timber;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistListAdapter extends SelectableAdapter<WishlistListAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;

    private List<Wishlist> wishlistList;
    private OnWishlistClickListener onWishlistClickListener;

    @Inject
    public WishlistListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.wishlistList = new ArrayList<>();
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

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

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
        this.wishlistList = wishlistList;
    }

    //qua prendo la roba passata dal fragment
    public void setOnWishlistClickListener(OnWishlistClickListener onWishlistClickListener) {
        this.onWishlistClickListener = onWishlistClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.wishlist_name)
        TextView wishlistNameTextView;

        @Bind(R.id.selected_overlay)
        View selectedOverlay;

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
        }
/*
        @Override
        public boolean onLongClick(View v) {
            Timber.d("long click");
            return false;
        }*/
    }


    //This is a pattern to declare an onClick in the ViewHolder but implement it in the fragment
    //E' un'interfaccia di wrapping
    //In Avengers: RecyclerClickListener
    public interface OnWishlistClickListener {
        public void onItemClick(View view , int position);

        public boolean onItemLongClick(View view, int position);
    }
}
