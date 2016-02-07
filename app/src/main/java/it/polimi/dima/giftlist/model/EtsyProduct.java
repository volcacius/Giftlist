package it.polimi.dima.giftlist.model;

/**
 * Created by Alessandro on 08/01/16.
 */
public class EtsyProduct {

    int listing_id;
    int category_id;
    String title;
    String description;

    public EtsyProduct(String item) {
        title = item;
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
