package it.polimi.dima.giftlist;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.wishlistsettings.WishlistSettingsActivity;

/**
 * Created by Alessandro on 15/03/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(WishlistSettingsActivity wishlistSettingsActivity);
}
