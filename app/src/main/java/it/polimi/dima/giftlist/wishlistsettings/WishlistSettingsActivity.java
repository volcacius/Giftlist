package it.polimi.dima.giftlist.wishlistsettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.polimi.dima.giftlist.R;

/**
 * Created by Elena on 10/02/2016.
 */
public class WishlistSettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_frame, new WishlistSettingsFragment())
                    .commit();
        }


    }


}