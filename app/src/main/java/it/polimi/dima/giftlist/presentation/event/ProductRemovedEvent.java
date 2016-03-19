package it.polimi.dima.giftlist.presentation.event;

import it.polimi.dima.giftlist.data.model.Product;

/**
 * Created by Alessandro on 18/03/16.
 */
public class ProductRemovedEvent {

    private Product product;

    public ProductRemovedEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
