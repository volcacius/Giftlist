package it.polimi.dima.giftlist.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandro on 08/01/16.
 */
public class Wishlist {

    public static final long DEFAULT_ID = 0;
    public static final int DEFAULT_ORDER = 0;

    private String name;
    private String occasion;
    private List<Product> productList;
    private long id;
    private int displayOrder;

    //Used by the DB when I'm retrieving an instance from it
    public Wishlist(long id, String name, String occasion, int displayOrder) {
        this.id = id;
        this.name = name;
        this.occasion = occasion;
        this.displayOrder = displayOrder;
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

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public static List<Wishlist> filter(List<Wishlist> wishlistList, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final List<Wishlist> filteredModelList = new ArrayList<>();
        for (Wishlist wishlist : wishlistList) {
            final String name = wishlist.getName().toLowerCase();
            final String occasion = wishlist.getOccasion().toLowerCase();
            if (name.contains(lowerCaseQuery) || occasion.contains(lowerCaseQuery)) {
                filteredModelList.add(wishlist);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wishlist wishlist = (Wishlist) o;

        if (id != wishlist.id) return false;
        if (!name.equals(wishlist.name)) return false;
        return occasion.equals(wishlist.occasion);

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
