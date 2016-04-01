package it.polimi.dima.giftlist.data.model;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbayProduct extends Product {

    String productPage;

    public EbayProduct(String name, String description, String listing_id, float price,
                       CurrencyType currencyType, String url_170x135, String url_page) {
        this.name = name;
        this.description = description;
        this.id = 0; //listing_id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl =  url_170x135;
        this.productPage = url_page;

    }

    public String getProductPage() {
        return productPage;
    }

    public void setProductPage(String productPage) {
        this.productPage = productPage;
    }
}
