package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.view.fragment.EtsyProductListFragment;

/**
 * Created by Elena on 19/01/2016.
 */
public class ProductListActivity extends AppCompatActivity {

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
            EtsyProductListFragment fragment = new EtsyProductListFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_frame, fragment)
                    .commit();
        }


    }

    public static Intent getCallingIntent(Context context, String category, String keywords) {
        Intent callingIntent = new Intent(context, ProductListActivity.class);
        callingIntent.putExtra(EXTRA_CATEGORY_SELECTED, category);
        callingIntent.putExtra(EXTRA_KEYWORDS, category);
        return callingIntent;
    }


}
