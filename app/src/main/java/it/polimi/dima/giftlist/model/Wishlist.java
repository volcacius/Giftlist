package it.polimi.dima.giftlist.model;

import java.util.List;

/**
 * Created by Alessandro on 08/01/16.
 */
public class Wishlist {


    private String wishlistTitle;
    private List<Product> productList;
    private int id;

    public Wishlist(List<Product> productList, int id) {
        this.productList = productList;
        this.id = id;
    }

    public Wishlist(String wishlistTitle) {
        this.wishlistTitle = wishlistTitle;
    }

    public String getWishlistTitle() {
        return wishlistTitle;
    }

    public void setWishlistTitle(String wishlistTitle) {
        this.wishlistTitle = wishlistTitle;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setItemList(List<Product> productList) {
        this.productList = productList;
    }

    public int getId() {
        return id;
    }
}
