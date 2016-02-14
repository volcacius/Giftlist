package it.polimi.dima.giftlist.model;

/**
 * Created by Alessandro on 08/01/16.
 */
public class EtsyProduct {

    int listing_id;
    int category_id;
    float price;



    String currency;
    String title;
    String description;
    String imageUrl;


    public EtsyProduct(String item) {
        title = item;
    }

    public EtsyProduct(EtsyProduct product) {
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.listing_id = product.getListing_id();
        this.price = product.getPrice();
        this.currency = product.getCurrency();
        this.imageUrl = product.getImageUrl();
    }

    public EtsyProduct(String title, String description, int listing_id, float price, String currency, String imageUrl) {
        this.title = title;
        this.description = description;
        this.listing_id = listing_id;
        this.price = price;
        this.currency = currency;
        this.imageUrl = imageUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListing_idAsString() {
        return "" + listing_id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getListing_id() {
        return listing_id;
    }

    public void setListing_id(int listing_id) {
        this.listing_id = listing_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
