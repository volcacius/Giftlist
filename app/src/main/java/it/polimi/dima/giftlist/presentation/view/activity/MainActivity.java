package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import it.polimi.dima.giftlist.GiftlistApplication;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistSettingsFragment;

/**
 * Created by Elena on 10/02/2016.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);
        if (savedInstanceState == null) {
            addFragment(R.id.activity_frame, new WishlistSettingsFragment(), null);
        }
    }
}