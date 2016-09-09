package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import javax.inject.Inject;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;

/**
 * Created by Alessandro on 08/09/16.
 */
public class MainActivity extends BaseActivity {

    private static final String FIRST_OPENING = "first_opening";

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        boolean firstOpening = sharedPreferences.getBoolean(FIRST_OPENING, true);
        if (firstOpening) {
            sharedPreferences.edit().putBoolean(FIRST_OPENING, false).apply();
            IntentStarter.startWelcomeActivity(this, true);
        } else {
            IntentStarter.startWishlistListActivity(this);
        }
    }
}
