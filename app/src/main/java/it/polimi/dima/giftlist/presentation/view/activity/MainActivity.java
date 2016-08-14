package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import javax.inject.Inject;

import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;

/**
 * Created by Elena on 10/02/2016.
 */
public class MainActivity extends BaseActivity {

    @Inject
    IntentStarter intentStarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        if (savedInstanceState == null) {
            intentStarter.startProductPickerSettingsActivity(this, 0);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    public void injectDependencies() {
        getApplicationComponent().inject(this);
    }
}