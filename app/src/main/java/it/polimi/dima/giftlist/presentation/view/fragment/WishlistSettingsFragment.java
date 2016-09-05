package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistSettingsPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistSettingsView;
import timber.log.Timber;

/**
 * Created by Elena on 10/08/2016.
 */

@FragmentWithArgs
public class WishlistSettingsFragment extends BaseMvpLceFragment<View, Wishlist, WishlistSettingsView, WishlistSettingsPresenter>
        implements WishlistSettingsView {

    private static final String DEFAULT_WISHLIST_NAME = "New Wishlist";

    @Arg
    long wishlistId;

    @Bind(R.id.settings_wishlist_name)
    EditText wishlistNameEditText;

    @Bind(R.id.settings_wishlist_occasion)
    Spinner wishlistOccasionSpinner;

    @Bind(R.id.button_start_product_picker_settings_activity)
    Button startProductPickerSettingsButton;

    Wishlist wishlist;
    String occasionSelected;

    @OnItemSelected(R.id.settings_wishlist_occasion)
    public void onItemSelected(int position) {
        occasionSelected = String.valueOf(wishlistOccasionSpinner.getSelectedItem());
    }

    @OnClick(R.id.button_start_product_picker_settings_activity)
    public void startProductPickerSettings() {
        String wishlistName = wishlistNameEditText.getText().toString();
        if (wishlistName.length() == 0) {
            wishlistName = DEFAULT_WISHLIST_NAME;
        }
        //If it is 0, it's a new wishlist
        if(wishlistId == Wishlist.DEFAULT_ID) {
            Random random = new Random();
            wishlistId = Math.abs(random.nextLong());
            getPresenter().addWishlist(wishlistId, wishlistName, occasionSelected);
            IntentStarter.startProductPickerSettingsActivity(getContext(), wishlistId);
        } else {
            getPresenter().updateWishlist(wishlistId, wishlistName, occasionSelected);
            IntentStarter.startWishlistActivity(getContext(), wishlistId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("ID of wishlist is: " + wishlistId);
        //wishlistId is passed as an arg
        //If it is zero, then an empty wishlist is created (since mosby LCE requires a piece of data to exist)
        //If it isn't zero, it is retrieved from the presenter in load data
        if (wishlistId == Wishlist.DEFAULT_ID) {
            wishlist = new Wishlist(Wishlist.DEFAULT_ID, "", "", Wishlist.DEFAULT_ORDER);
        }
        setRetainInstance(true);
    }

    @Override
    public WishlistSettingsPresenter createPresenter() {
        return getComponent(WishlistComponent.class).provideWishlistSettingsPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wishlist_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    protected void injectDependencies() {
        //No field dependencies to inject
    }

    @Override
    public void setData(Wishlist data) {
        wishlist = data;
        if (wishlist.getId() != Wishlist.DEFAULT_ID) {
            wishlistNameEditText.setText(data.getName());
            wishlistOccasionSpinner.setSelection(((ArrayAdapter)(wishlistOccasionSpinner.getAdapter())).getPosition(data.getOccasion()));
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (wishlist.getId() == Wishlist.DEFAULT_ID) {
            showContent();
        } else {
            presenter.subscribe(false);
        }
    }
}
