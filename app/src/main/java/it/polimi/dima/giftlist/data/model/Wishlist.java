package it.polimi.dima.giftlist.data.model;

import android.content.Context;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polimi.dima.giftlist.R;

/**
 * Created by Alessandro on 08/01/16.
 */
public class Wishlist implements Comparable<Wishlist> {

    public static final long DEFAULT_ID = 0;
    public static final int DEFAULT_ORDER = 0;

    private String name;
    private String occasion;
    private String keyword;
    private String age;
    private Float minPrice;
    private Float maxPrice;
    private List<Product> productList;
    private long id;
    private int displayOrder;

    //Used by the DB when I'm retrieving an instance from it
    public Wishlist(long id, String name, String occasion, int displayOrder, String keyword, String age, Float minPrice, Float maxPrice) {
        this.id = id;
        this.name = name;
        this.occasion = occasion;
        this.displayOrder = displayOrder;
        this.keyword = keyword;
        this.age = age;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

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

    public String getKeyword() {
        return keyword;
    }

    public String getAge() {
        return age;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
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

    public static LinkedList<Wishlist> filter(List<Wishlist> wishlistList, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final LinkedList<Wishlist> filteredModelList = new LinkedList<>();
        for (Wishlist wishlist : wishlistList) {
            final String name = wishlist.getName().toLowerCase();
            final String occasion = wishlist.getOccasion().toLowerCase();
            if (name.contains(lowerCaseQuery) || occasion.contains(lowerCaseQuery)) {
                filteredModelList.add(wishlist);
            }
        }
        return filteredModelList;
    }

    public static int getWishlistThumbnail(Context context, String occasion) {
        if (occasion.equals(context.getString(R.string.birthday))) {
            return R.drawable.birthday;
        } else if (occasion.equals(context.getString(R.string.anniversary))) {
            return R.drawable.cake_anniversary;
        } else if (occasion.equals(context.getString(R.string.graduation))) {
            return R.drawable.beer;
        } else if (occasion.equals(context.getString(R.string.wedding))) {
            return R.drawable.wife;
        } else {
            return R.drawable.lights;
        }
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

    @Override
    public int compareTo(Wishlist another) {
        return another.getDisplayOrder() - getDisplayOrder();
    }
}
