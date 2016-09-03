package it.polimi.dima.giftlist.data.model;

import java.util.List;

/**
 * Created by Alessandro on 08/01/16.
 */
public class Wishlist {

    public static final long DEFAULT_ID = 0;

    private String name;
    private String occasion;
    private List<Product> productList;
    private long id;


    public Wishlist(long id, String name, String occasion) {
        this.id = id;
        this.name = name;
        this.occasion = occasion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void appendProductList(List<Product> productList) {
        this.productList.addAll(productList);
    }

    public long getId() {
        return id;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
