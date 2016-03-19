package it.polimi.dima.giftlist.presentation.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import it.polimi.dima.giftlist.data.repository.ProductRepository;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import it.polimi.dima.giftlist.presentation.module.ApplicationModule;
import it.polimi.dima.giftlist.presentation.view.activity.BaseActivity;
import it.polimi.dima.giftlist.presentation.view.activity.BaseViewStateLceActivity;
import it.polimi.dima.giftlist.presentation.view.activity.MainActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.BaseFragment;
import it.polimi.dima.giftlist.presentation.view.fragment.BaseViewStateLceFragment;

/**
 * Created by Alessandro on 15/03/16.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);
    void inject(BaseFragment baseFragment);

    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    ProductRepository productRepository();
}
