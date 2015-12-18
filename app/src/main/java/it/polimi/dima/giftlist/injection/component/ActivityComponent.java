package it.polimi.dima.giftlist.injection.component;

import dagger.Component;
import it.polimi.dima.giftlist.injection.PerActivity;
import it.polimi.dima.giftlist.injection.module.ActivityModule;
import it.polimi.dima.giftlist.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
