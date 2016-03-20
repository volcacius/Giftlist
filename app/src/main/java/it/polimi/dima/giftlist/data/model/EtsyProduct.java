package it.polimi.dima.giftlist.data.model;

/**
 * Created by Alessandro on 08/01/16.
 */
public class EtsyProduct extends Product {

    int listingId;
    int categoryId;

    public EtsyProduct(String name, String description, int listingId, float price, String currency, String imageUrl) {
        this.name = name;
        this.description = description;
        this.listingId = listingId;
        this.price = price;
        this.currency = currency;
        this.imageUrl = imageUrl;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
