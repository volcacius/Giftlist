package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistSettingsComponent;
import it.polimi.dima.giftlist.presentation.module.WishlistSettingsModule;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistListFragment;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistSettingsFragmentBuilder;
import timber.log.Timber;

/**
 * Created by Elena on 10/08/2016.
 */
public class WishlistSettingsActivity extends BaseActivity implements HasComponent<WishlistSettingsComponent> {

    private static final String EXTRA_WISHLIST_ID = "wishlist_id";
    long wishlistId;
    WishlistSettingsComponent wishlistSettingsComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_settings);
        ButterKnife.bind(this);

        //I need the wishlist id to launch the fragment
        //If it's the first time creating the activity, I get it from the Intent.
        //If the activity is recreated e.g. after rotation, it is restored by IcePick in the super.onCreate call
        if (savedInstanceState == null) {
            wishlistId = getIntent().getLongExtra(EXTRA_WISHLIST_ID, Wishlist.DEFAULT_ID);
        }
        createComponent();
        if (savedInstanceState == null) {
            addFragment(R.id.wishlist_settings_activity_content, new WishlistSettingsFragmentBuilder(wishlistId).build());
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wishlist_settings;
    }

    public static Intent getCallingIntent(Context context, long wishlistId) {
        Intent callingIntent = new Intent(context, WishlistSettingsActivity.class);
        callingIntent.putExtra(EXTRA_WISHLIST_ID, wishlistId);
        return callingIntent;
    }

    @Override
    public WishlistSettingsComponent getComponent() {
        return wishlistSettingsComponent;
    }

    @Override
    public void createComponent() {
        wishlistSettingsComponent = getApplicationComponent().plus(new WishlistSettingsModule());
    }
}
