package it.polimi.dima.giftlist.data.model;

/**
 * Created by Alessandro on 08/01/16.
 */
public class EtsyProduct extends Product {

    int categoryId;

    public EtsyProduct(String name, String description, int id, float price, CurrencyType currencyType, String imageUrl) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl = imageUrl;
    }

    public int getListingId() {
        return id;
    }

    public void setListingId(int listingId) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
