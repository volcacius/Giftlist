package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;
import icepick.Icepick;
import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.GiftlistApplication;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;

/**
 * Created by Alessandro on 18/03/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Inject
    IntentStarter intentStarter;
    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    //TODO: approfondire differenza fra add fragment e replace fragment
    //Attenzione anche alla questione event bus
    //http://stackoverflow.com/questions/18634207/difference-between-add-replace-and-addtobackstack
    protected void addFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
        .add(containerViewId, fragment)
        .commit();
    }

    /*
     * Inject the dependencies required by this base classe. The component itself is declared in the Application class
     * so its lifecycle is fine since it's tied to the Application.
     * This method can be overriden by the subclasses without calling the super (i.e. this one) implementation since each component
     * of the subclasses is declared as a subcomponent of the application component, so it can access the application component modules.
     */
    protected void injectDependencies() {
        this.getApplicationComponent().inject(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((GiftlistApplication) getApplication()).getApplicationComponent();
    }
}
