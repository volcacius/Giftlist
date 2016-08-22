package it.polimi.dima.giftlist.presentation.component;

import dagger.Subcomponent;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.module.ProductPickerSettingsModule;
import it.polimi.dima.giftlist.presentation.presenter.ProductPickerSettingsPresenter;
import it.polimi.dima.giftlist.presentation.view.activity.ProductPickerSettingsActivity;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerSettingsFragment;


/**
 * Created by Elena on 22/08/2016.
 */
@PerActivity
@Subcomponent(modules = {ProductPickerSettingsModule.class})
public interface ProductPickerSettingsComponent {


    public void inject(ProductPickerSettingsActivity activity);
    public void inject(ProductPickerSettingsFragment fragment);

    public ProductPickerSettingsPresenter providePresenter();
}
