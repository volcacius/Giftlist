package it.polimi.dima.giftlist.presentation.event;

import it.polimi.dima.giftlist.data.model.Product;

/**
 * Created by Elena on 19/08/2016.
 */
public class ProductImageSavedEvent {

    private Product product;

    public ProductImageSavedEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
