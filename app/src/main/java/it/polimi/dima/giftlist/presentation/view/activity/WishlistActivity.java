package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistComponent;
import it.polimi.dima.giftlist.presentation.module.WishlistModule;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistFragmentBuilder;
import timber.log.Timber;

/**
 * Created by Alessandro on 24/04/16.
 */
public class WishlistActivity extends BaseActivity implements HasComponent<WishlistComponent> {

    private static final String EXTRA_WISHLIST_ID = "wishlist_id";
    long wishlistId;

    WishlistComponent wishlistComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //I need the wishlist id to launch the fragment
        //If it's the first time creating the activity, I get it from the Intent.
        //If the activity is recreated e.g. after rotation, it is restored by IcePick in the super.onCreate call
        if (savedInstanceState == null) {
            wishlistId = getIntent().getLongExtra(EXTRA_WISHLIST_ID, Wishlist.DEFAULT_ID);
        } 
        createComponent();
        if (savedInstanceState == null) {
            addFragment(R.id.wishlist_activity_content, new WishlistFragmentBuilder(wishlistId).build());
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wishlist;
    }

    //I need to expose the component so that I can perform injection from the fragment
    @Override
    public WishlistComponent getComponent() {
        return wishlistComponent;
    }

    @Override
    public void createComponent() {
        wishlistComponent = getApplicationComponent().plus(new WishlistModule(this, wishlistId));
    }

    public static Intent getCallingIntent(Context context, long wishlistId) {
        Intent callingIntent = new Intent(context, WishlistActivity.class);
        callingIntent.putExtra(EXTRA_WISHLIST_ID, wishlistId);
        return callingIntent;
    }
}
