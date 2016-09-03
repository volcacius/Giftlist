package it.polimi.dima.giftlist.data.model;

import android.os.Parcel;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Alessandro on 08/01/16.
 */

public class EtsyProduct extends Product {

    public EtsyProduct(String name, String description, long id, float price, CurrencyType currencyType, String imageUrl, String imageUri, String url_page) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl = imageUrl;
        this.imageUri = imageUri;
        this.productPage = url_page;
    }

    public EtsyProduct(String name, String description, long id, float price, float convertedPrice, CurrencyType currencyType, String imageUrl, String imageUri, String productPage, long wishlistId) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.price = price;
        this.convertedPrice = convertedPrice;
        this.currencyType = currencyType;
        this.imageUrl = imageUrl;
        this.imageUri = imageUri;
        this.productPage = productPage;
        this.wishlistId = wishlistId;
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
