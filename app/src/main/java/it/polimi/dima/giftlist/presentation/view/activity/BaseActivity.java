package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;

/**
 * Created by Alessandro on 18/03/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /* Since most activities do not inject stuff, do not put either an abstract injectDependencies()
       nor injected fields, leave it to subclasses */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    //TODO: approfondire differenza fra add fragment e replace fragment
    //Attenzione anche alla questione event bus
    //http://stackoverflow.com/questions/18634207/difference-between-add-replace-and-addtobackstack
    protected void addFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
        .add(containerViewId, fragment)
        .commit();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((GiftlistApplication) getApplication()).getApplicationComponent();
    }

}
