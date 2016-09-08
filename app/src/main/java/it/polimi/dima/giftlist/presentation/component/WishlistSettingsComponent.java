package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.module.WishlistModule;
import it.polimi.dima.giftlist.presentation.module.WishlistSettingsModule;
import it.polimi.dima.giftlist.presentation.presenter.WishlistSettingsPresenter;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistSettingsActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistSettingsFragment;


/**
 * Created by Elena on 22/08/2016.
 */
@PerActivity
@Subcomponent(modules = {WishlistSettingsModule.class})
public interface WishlistSettingsComponent {


    public void inject(WishlistSettingsActivity activity);
    public void inject(WishlistSettingsFragment fragment);

    public WishlistSettingsPresenter provideWishlistSettingsPresenter();
}
