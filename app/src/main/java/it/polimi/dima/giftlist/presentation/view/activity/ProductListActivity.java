package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.ProductListComponent;
import it.polimi.dima.giftlist.presentation.module.ProductListModule;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductListFragment;

/**
 * Created by Elena on 19/01/2016.
 */
public class ProductListActivity extends BaseActivity implements HasComponent<ProductListComponent> {

    private static final String EXTRA_CATEGORY_SELECTED = "category";
    private static final String EXTRA_KEYWORDS = "keywords";

    private String category;
    private String keywords;

    //The component has to be declared in the activity and not in the fragment since its lifecycle must be tied to the activity
    ProductListComponent productListComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);
        //I need category and keywords both for createComponent() and to launch the fragment
        //If it's the first time creating the activity, I get them from the Intent.
        //If the activity is recreated e.g. after rotation, they are restored by IcePick in the super.onCreate call
        if (savedInstanceState == null) {
            category = getIntent().getStringExtra(EXTRA_CATEGORY_SELECTED);
            keywords = getIntent().getStringExtra(EXTRA_KEYWORDS);
        }
        createComponent();
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putString(EXTRA_CATEGORY_SELECTED,category);
            args.putString(EXTRA_KEYWORDS,keywords);
            addFragment(R.id.activity_frame, new ProductListFragment(), args);
        }

    }

    @DebugLog
    public static Intent getCallingIntent(Context context, String category, String keywords) {
        Intent callingIntent = new Intent(context, ProductListActivity.class);
        callingIntent.putExtra(EXTRA_CATEGORY_SELECTED, category);
        callingIntent.putExtra(EXTRA_KEYWORDS, keywords);
        return callingIntent;
    }

    //I need to expose to component so that I can perform injection from the fragment
    @Override
    public ProductListComponent getComponent() {
        return productListComponent;
    }

    protected void createComponent() {
        productListComponent = getApplicationComponent().plus(new ProductListModule(this, category, keywords));
    }
}
