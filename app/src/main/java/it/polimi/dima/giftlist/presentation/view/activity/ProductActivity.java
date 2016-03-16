package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductFragment;

/**
 * Created by Elena on 19/01/2016.
 */
public class ProductActivity extends AppCompatActivity{

    private static final String EXTRA_CATEGORY_SELECTED = "category";
    private static final String EXTRA_KEYWORDS = "keywords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);

        if (savedInstanceState == null) {
            String category = getIntent().getStringExtra(EXTRA_CATEGORY_SELECTED);
            String keywords = getIntent().getStringExtra(EXTRA_KEYWORDS);
            Bundle args = new Bundle();
            args.putString(EXTRA_CATEGORY_SELECTED,category);
            args.putString(EXTRA_KEYWORDS,keywords);
            ProductFragment fragment = new ProductFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_frame, fragment)
                    .commit();
        }


    }

    public static void start(Context context, String categorySelected, String keywords) {
        Intent productIntent = new Intent(context, ProductActivity.class);
        productIntent.putExtra(EXTRA_CATEGORY_SELECTED, categorySelected);
        productIntent.putExtra(EXTRA_KEYWORDS, keywords);
        context.startActivity(productIntent);
    }


}
