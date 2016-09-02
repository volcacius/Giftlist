package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import clojure.lang.Compiler;
import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CategoryType;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.ProductPickerComponent;
import it.polimi.dima.giftlist.presentation.module.ProductPickerModule;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerFragmentBuilder;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerSettingsFragment;
import timber.log.Timber;


/**
 * Created by Elena on 19/01/2016.
 */
public class ProductPickerActivity extends BaseActivity implements HasComponent<ProductPickerComponent> {

    private static final String EXTRA_REPOSITORIES_SELECTED = "repositories";
    private static final String EXTRA_CHOSEN_CATEGORIES_SELECTED = "chosen_categories";
    private static final String EXTRA_KEYWORDS = "keywords";
    private static final String EXTRA_MAXPRICE = "maxprice";
    private static final String EXTRA_MINPRICE = "minprice";
    private static final String EXTRA_WISHLIST_ID = "wishlist_id";
    private long wishlistId;
    private HashMap<Class, Boolean> enabledRepositoryMap;
    private ArrayList<CategoryType> chosenCategoriesList;
    private String keywords;
    private Float maxprice;
    private Float minprice;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //The component has to be declared in the activity and not in the fragment since its lifecycle must be tied to the activity
    ProductPickerComponent productPickerComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        //I need repositories, category and keywords both for createComponent() and to launch the fragment
        //If it's the first time creating the activity, I get them from the Intent.
        //If the activity is recreated e.g. after rotation, they are restored by IcePick in the super.onCreate call
        if (savedInstanceState == null) {
            enabledRepositoryMap = (HashMap<Class, Boolean>) getIntent().getSerializableExtra(EXTRA_REPOSITORIES_SELECTED);
            chosenCategoriesList = (ArrayList<CategoryType>) getIntent().getSerializableExtra(EXTRA_CHOSEN_CATEGORIES_SELECTED);
            keywords = getIntent().getStringExtra(EXTRA_KEYWORDS);
            maxprice = getIntent().getFloatExtra(EXTRA_MAXPRICE, (float) 1000.00);
            minprice = getIntent().getFloatExtra(EXTRA_MINPRICE, (float) 0.00);
            wishlistId = getIntent().getLongExtra(EXTRA_WISHLIST_ID, Wishlist.DEFAULT_ID);
        }
        createComponent();
        if (savedInstanceState == null) {
            addFragment(R.id.product_picker_activity_content, new ProductPickerFragmentBuilder(chosenCategoriesList, enabledRepositoryMap, keywords, wishlistId).build());
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_picker;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wishlist:
                //implemented in the fragment
                return false;

            case R.id.action_settings:
                //implemented in the fragment
                return false;

            default:
                Timber.d("default option from activity");
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @DebugLog
    public static Intent getCallingIntent(Context context, HashMap<Class, Boolean> enabledRepositoryMap, ArrayList<CategoryType> chosenCategoriesList, String keywords, Float maxprice, Float minprice, Long wishlistId) {
        Intent callingIntent = new Intent(context, ProductPickerActivity.class);
        callingIntent.putExtra(EXTRA_REPOSITORIES_SELECTED, enabledRepositoryMap);
        callingIntent.putExtra(EXTRA_CHOSEN_CATEGORIES_SELECTED, chosenCategoriesList);
        //All standard implementations of java.util.List already implement java.io.Serializable.
        //So even though java.util.List itself is not a subtype of java.io.Serializable, it should be safe to cast the list to Serializable,
        //as long as you know it's one of the standard implementations like ArrayList or LinkedList.
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
        productPickerComponent = getApplicationComponent().plus(new ProductPickerModule(this, enabledRepositoryMap, chosenCategoriesList, keywords, maxprice, minprice, wishlistId));
    }
}
