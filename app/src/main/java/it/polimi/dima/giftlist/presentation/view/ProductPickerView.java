package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;

/**
 * Created by Elena on 27/01/2016.
 */
public interface ProductPickerView extends MvpLceView<List<Product>> {

    void appendData(List<Product> data);

    void showProductAddedError();

    void showProductAddedSuccess();

    int getProductDisplayOrder();

    void setNextProductDisplayOrder();
}
