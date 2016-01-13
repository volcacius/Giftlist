package it.polimi.dima.giftlist.wishlistlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.polimi.dima.giftlist.FirstModule;
import it.polimi.dima.giftlist.R;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_frame, new WishlistListFragment())
                    .commit();
        }
    }

}
