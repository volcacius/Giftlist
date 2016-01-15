package it.polimi.dima.giftlist.model;

import java.util.List;

/**
 * Created by Alessandro on 08/01/16.
 */
public class Wishlist {

    private List<Item> mItemList;
    private int id;

    public Wishlist(List<Item> mItemList, int id) {
        this.mItemList = mItemList;
        this.id = id;
    }

    public List<Item> getItemList() {
        return mItemList;
    }

    public void setItemList(List<Item> itemList) {
        mItemList = itemList;
    }

    public int getId() {
        return id;
    }
}
