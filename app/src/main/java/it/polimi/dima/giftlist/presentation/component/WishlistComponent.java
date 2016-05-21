package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.module.WishlistModule;
import it.polimi.dima.giftlist.presentation.presenter.WishlistPresenter;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistFragment;

/**
 * Created by Alessandro on 24/04/16.
 */
@PerActivity
@Subcomponent(modules = {WishlistModule.class})
public interface WishlistComponent {
    void inject(WishlistActivity wishlistActivity);
    void inject(WishlistFragment wishlistFragment);

    WishlistPresenter provideWishlistPresenter();
}
