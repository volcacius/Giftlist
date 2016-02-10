package it.polimi.dima.giftlist.product;

import android.content.Context;
import android.content.Intent;
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

    private static final String EXTRA_CATEGORY_SELECTED = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);

        if (savedInstanceState == null) {
            String category = getIntent().getStringExtra(EXTRA_CATEGORY_SELECTED);
            Bundle args = new Bundle();
            args.putString(EXTRA_CATEGORY_SELECTED,category);
            ProductFragment fragment = new ProductFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_frame, fragment)
                    .commit();
        }


    }

    public static void start(Context context, String categorySelected) {
        Intent productIntent = new Intent(context, ProductActivity.class);
        productIntent.putExtra(EXTRA_CATEGORY_SELECTED, categorySelected);
        context.startActivity(productIntent);
    }


}
