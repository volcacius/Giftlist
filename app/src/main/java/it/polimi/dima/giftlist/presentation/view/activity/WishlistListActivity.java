package it.polimi.dima.giftlist.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.di.HasComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistListComponent;
import it.polimi.dima.giftlist.presentation.module.WishlistListModule;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistListFragment;
import timber.log.Timber;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListActivity extends BaseActivity implements HasComponent<WishlistListComponent> {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    WishlistListComponent wishlistListComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistlist);
        createComponent();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            addFragment(R.id.wishlistlist_activity_content, new WishlistListFragment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wishlistlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                 //implemented in the fragment
                return false;
            default:
                break;
        }
        return false;
    }

    //I need to expose the component so that I can perform injection from the fragment
    @Override
    public WishlistListComponent getComponent() {
        return wishlistListComponent;
    }

    protected void createComponent() {
        wishlistListComponent = getApplicationComponent().plus(new WishlistListModule(this));
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, WishlistListActivity.class);
        return callingIntent;
    }
}
