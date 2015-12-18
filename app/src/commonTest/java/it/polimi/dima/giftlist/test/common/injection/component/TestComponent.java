package it.polimi.dima.giftlist.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.injection.component.ApplicationComponent;
import it.polimi.dima.giftlist.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
