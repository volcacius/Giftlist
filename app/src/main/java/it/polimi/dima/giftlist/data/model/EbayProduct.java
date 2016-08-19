package it.polimi.dima.giftlist.data.model;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbayProduct extends Product {

    String productPage;

    public EbayProduct(String name, String description, long listing_id, float price,
                       CurrencyType currencyType, String url_170x135, String url_page, String imageUri) {
        this.name = name;
        this.description = description;
        this.id = listing_id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl =  url_170x135;
        this.productPage = url_page;
        this.imageUri = imageUri;

    }

    public EbayProduct(String name, String description, long id, float price, CurrencyType currencyType, String imageUrl, String imageUri, long wishlistId) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl = imageUrl;
        this.wishlistId = wishlistId;
        this.imageUri = imageUri;
    }

    public String getProductPage() {
        return productPage;
    }
}
