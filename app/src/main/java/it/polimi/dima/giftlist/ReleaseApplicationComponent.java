package it.polimi.dima.giftlist;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.data.module.CurrencyModule;
import it.polimi.dima.giftlist.data.module.EbayModule;
import it.polimi.dima.giftlist.data.module.EtsyModule;
import it.polimi.dima.giftlist.data.module.ReleaseCurrencyModule;
import it.polimi.dima.giftlist.data.module.ReleaseEbayModule;
import it.polimi.dima.giftlist.data.module.ReleaseEtsyModule;

/**
 * Created by Alessandro on 24/03/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ReleaseCurrencyModule.class, ReleaseEtsyModule.class, ReleaseEbayModule.class, CurrencyModule.class, EtsyModule.class, EbayModule.class})
public interface ReleaseApplicationComponent extends ApplicationComponent {
}
