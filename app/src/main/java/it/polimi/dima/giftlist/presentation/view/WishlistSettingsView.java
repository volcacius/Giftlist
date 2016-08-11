package it.polimi.dima.giftlist.presentation.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Elena on 10/08/2016.
 */
public interface WishlistSettingsView extends MvpView{

    void showWishlistAddedError();

    void showWishlistAddedSuccess();
}
