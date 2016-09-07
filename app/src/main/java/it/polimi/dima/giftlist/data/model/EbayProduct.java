package it.polimi.dima.giftlist.data.model;

import android.os.Parcel;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbayProduct extends Product {

    public EbayProduct(String name, String description, long listing_id, float price,
                       CurrencyType currencyType, String url_170x135, String imageUri, String url_page) {
        this.name = name;
        this.description = description;
        this.id = listing_id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl =  url_170x135;
        this.productPage = url_page;
        this.imageUri = imageUri;

    }

    public EbayProduct(String name, String description, long id, float price, float convertedPrice, CurrencyType currencyType, String imageUrl, String imageUri, String productPage, long wishlistId, int displayOrder) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.price = price;
        this.convertedPrice = convertedPrice;
        this.currencyType = currencyType;
        this.imageUrl = imageUrl;
        this.wishlistId = wishlistId;
        this.imageUri = imageUri;
        this.productPage = productPage;
        this.primaryKeyId = id + "-" + wishlistId;
        this.displayOrder = displayOrder;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel source) {
            Product target = new Product();
            ProductParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
