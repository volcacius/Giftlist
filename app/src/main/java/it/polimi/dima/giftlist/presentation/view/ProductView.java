package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import it.polimi.dima.giftlist.data.model.EtsyProduct;

/**
 * Created by Elena on 27/01/2016.
 */
public interface ProductView extends MvpLceView<List<EtsyProduct>> {
    //useful for methods to change it dynamically from other activities
}
