package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.ProductPickerComponent;
import it.polimi.dima.giftlist.presentation.module.ProductPickerModule;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerFragmentBuilder;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerSettingsFragment;


/**
 * Created by Elena on 19/01/2016.
 */
public class ProductPickerActivity extends BaseActivity implements HasComponent<ProductPickerComponent> {

    private static final String EXTRA_REPOSITORIES_SELECTED = "repositories";
    private static final String EXTRA_CATEGORY_SELECTED = "category";
    private static final String EXTRA_KEYWORDS = "keywords";
    private static final String EXTRA_MAXPRICE = "maxprice";
    private static final String EXTRA_MINPRICE = "minprice";
    private static final String EXTRA_WISHLIST_ID = "wishlist_id";
    private long wishlistId;
    private HashMap<Class, Boolean> enabledRepositoryMap;
    private String category;
    private String keywords;
    private Float maxprice;
    private Float minprice;

    //The component has to be declared in the activity and not in the fragment since its lifecycle must be tied to the activity
    ProductPickerComponent productPickerComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //I need repositories, category and keywords both for createComponent() and to launch the fragment
        //If it's the first time creating the activity, I get them from the Intent.
        //If the activity is recreated e.g. after rotation, they are restored by IcePick in the super.onCreate call
        if (savedInstanceState == null) {
            enabledRepositoryMap = (HashMap<Class, Boolean>) getIntent().getSerializableExtra(EXTRA_REPOSITORIES_SELECTED);
            category = getIntent().getStringExtra(EXTRA_CATEGORY_SELECTED);
            keywords = getIntent().getStringExtra(EXTRA_KEYWORDS);
            maxprice = getIntent().getFloatExtra(EXTRA_MAXPRICE, (float) 1000.00);
            minprice = getIntent().getFloatExtra(EXTRA_MINPRICE, (float) 0.00);
            wishlistId = getIntent().getLongExtra(EXTRA_WISHLIST_ID, Wishlist.DEFAULT_ID);
        }
        createComponent();
        if (savedInstanceState == null) {
            addFragment(R.id.activity_frame, new ProductPickerFragmentBuilder(category, enabledRepositoryMap, keywords, wishlistId).build());
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_picker;
    }

    @DebugLog
    public static Intent getCallingIntent(Context context, HashMap<Class, Boolean> enabledRepositoryMap, String category, String keywords, Float maxprice, Float minprice, Long wishlistId) {
        Intent callingIntent = new Intent(context, ProductPickerActivity.class);
        callingIntent.putExtra(EXTRA_REPOSITORIES_SELECTED, enabledRepositoryMap);
        callingIntent.putExtra(EXTRA_CATEGORY_SELECTED, category);
        callingIntent.putExtra(EXTRA_KEYWORDS, keywords);
        callingIntent.putExtra(EXTRA_MAXPRICE, maxprice);
        callingIntent.putExtra(EXTRA_MINPRICE, minprice);
        callingIntent.putExtra(EXTRA_WISHLIST_ID, wishlistId);
        return callingIntent;
    }

    @Override
    public ProductPickerComponent getComponent() {
        return productPickerComponent;
    }

    @Override
    public void createComponent() {
        productPickerComponent = getApplicationComponent().plus(new ProductPickerModule(this, enabledRepositoryMap, category, keywords, maxprice, minprice, wishlistId));
    }
}
