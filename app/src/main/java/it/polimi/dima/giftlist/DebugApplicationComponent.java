package it.polimi.dima.giftlist;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.data.module.CurrencyModule;
import it.polimi.dima.giftlist.data.module.DbModule;
import it.polimi.dima.giftlist.data.module.DebugCurrencyModule;
import it.polimi.dima.giftlist.data.module.DebugEtsyModule;
import it.polimi.dima.giftlist.data.module.EtsyModule;

/**
 * Created by Alessandro on 24/03/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class, DebugCurrencyModule.class, DebugEtsyModule.class, CurrencyModule.class, EtsyModule.class, DbModule.class})
public interface DebugApplicationComponent extends ApplicationComponent {
}
