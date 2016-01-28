package it.polimi.dima.giftlist.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.wishlistlist.WishlistListFragment;

/**
 * Created by Elena on 19/01/2016.
 */
public class ProductActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_frame, new ProductFragment())
                    .commit();
        }


    }


}
