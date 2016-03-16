package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.model.Wishlist;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListAdapter extends SupportAnnotatedAdapter implements WishlistListAdapterBinder {


    @ViewType(
            layout = R.layout.list_wishlistlist,
            views = {
                    @ViewField(id = R.id.wishlist_title, type = TextView.class, name = "title")
            }) public final int wishlist = 0;

    List<Wishlist> mWishlistList;

    @Inject
    public WishlistListAdapter(Context context) {
        super(context);
    }

    @Override public int getItemCount() {
        return mWishlistList == null ? 0 : mWishlistList.size();
    }

    public List<Wishlist> getWishlistList() {
        return mWishlistList;
    }

    public void setWishlistList(List<Wishlist> repos) {
        this.mWishlistList = repos;
    }

    @Override public void bindViewHolder(WishlistListAdapterHolders.WishlistViewHolder vh, int position) {
        Wishlist repo = mWishlistList.get(position);

        vh.title.setText(repo.getWishlistTitle());
    }

}
