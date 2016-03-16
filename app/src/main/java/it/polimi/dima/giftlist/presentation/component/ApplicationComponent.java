package it.polimi.dima.giftlist.presentation.component;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.presentation.module.ApplicationModule;
import it.polimi.dima.giftlist.presentation.view.activity.WishlistSettingsActivity;

/**
 * Created by Alessandro on 15/03/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(WishlistSettingsActivity wishlistSettingsActivity);
}
