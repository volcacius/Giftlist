package it.polimi.dima.giftlist.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;

/**
 * Created by Alessandro on 08/01/16.
 */
@ParcelablePlease
public class Product implements Parcelable {

    public static final long DEFAULT_ID = 0L;
    String name;
    float price;
    float convertedPrice;
    String primaryKeyId;
    long id;
    CurrencyType currencyType;
    String description;
    String imageUrl;
    String imageUri;
    String productPage;
    long wishlistId;



    public String getPrimaryKeyId() {
        return primaryKeyId;
    }

    public void setPrimaryKeyId(String primaryKeyId) {
        this.primaryKeyId = primaryKeyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public String getDescription() {
        return description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public float getPrice() {
        return price;
    }

    public float getConvertedPrice() {
        return convertedPrice;
    }

    public void setConvertedPrice(float price) {
        this.convertedPrice = price;
    }

    public long getId() {
        return id;
    }

    public long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getProductPage() {
        return productPage;
    }

    public void setProductPage(String productPage) {
        this.productPage = productPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        ProductParcelablePlease.writeToParcel(this, dest, flags);
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