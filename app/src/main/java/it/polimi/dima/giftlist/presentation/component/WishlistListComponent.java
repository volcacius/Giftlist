package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.presentation.module.WishlistListModule;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.presenter.WishlistListPresenter;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistListActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistListFragment;

/**
 * Created by Elena on 12/01/2016.
 */
@PerActivity
@Subcomponent(modules = {WishlistListModule.class})
public interface WishlistListComponent {

    public void inject(WishlistListFragment fragment);
    public void inject(WishlistListActivity activity);

    public WishlistListPresenter providePresenter();


}


