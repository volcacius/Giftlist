package it.polimi.dima.giftlist.model;

import java.util.List;

/**
 * Created by Alessandro on 08/01/16.
 */
public class Wishlist {


    private String mWishlistTitle;
    private List<Item> mItemList;

    public Wishlist(String wishlistTitle) {
        mWishlistTitle = wishlistTitle;
    }

    public String getWishlistTitle() {
        return mWishlistTitle;
    }

    public void setWishlistTitle(String wishlistTitle) {
        mWishlistTitle = wishlistTitle;
    }

    public List<Item> getItemList() {
        return mItemList;
    }

    public void setItemList(List<Item> itemList) {
        mItemList = itemList;
    }
}
