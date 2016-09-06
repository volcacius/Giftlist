package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.module.WishlistListModule;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistListFragment;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListActivity extends BaseActivity implements HasComponent<WishlistListComponent> {

    WishlistListComponent wishlistListComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createComponent();
        if (savedInstanceState == null) {
            addFragment(R.id.wishlistlist_activity_content, new WishlistListFragment());
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wishlistlist;
    }

    //I need to expose the component so that I can perform injection from the fragment
    @Override
    public WishlistListComponent getComponent() {
        return wishlistListComponent;
    }

    @Override
    public void createComponent() {
        wishlistListComponent = getApplicationComponent().plus(new WishlistListModule(this));
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, WishlistListActivity.class);
        return callingIntent;
    }
}
