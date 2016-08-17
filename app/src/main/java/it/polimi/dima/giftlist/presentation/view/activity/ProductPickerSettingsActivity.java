package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerSettingsFragmentBuilder;

/**
 * Created by Alessandro on 02/05/16.
 */
public class ProductPickerSettingsActivity extends BaseActivity implements HasComponent<ApplicationComponent> {

    private static final String EXTRA_WISHLIST_ID = "wishlist_id";
    long wishlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_picker_settings);
        //I need the wishlist id to launch the fragment
        //If it's the first time creating the activity, I get it from the Intent.
        //If the activity is recreated e.g. after rotation, it is restored by IcePick in the super.onCreate call
        if (savedInstanceState == null) {
            wishlistId = getIntent().getLongExtra(EXTRA_WISHLIST_ID, Wishlist.DEFAULT_ID);
        }
        if (savedInstanceState == null) {
            addFragment(R.id.product_picker_settings_activity_content, new ProductPickerSettingsFragmentBuilder(wishlistId).build());
        }

    }

    public static Intent getCallingIntent(Context context, long wishlistId) {
        Intent callingIntent = new Intent(context, ProductPickerSettingsActivity.class);
        callingIntent.putExtra(EXTRA_WISHLIST_ID, wishlistId);
        return callingIntent;
    }

    @Override
    public ApplicationComponent getComponent() {
        return getApplicationComponent();
    }
}
