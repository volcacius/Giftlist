package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.presentation.component.WishlistSettingsComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.WishlistSettingsPresenter;
import it.polimi.dima.giftlist.presentation.view.WishlistListView;
import it.polimi.dima.giftlist.presentation.view.WishlistSettingsView;
import timber.log.Timber;

/**
 * Created by Elena on 10/08/2016.
 */

@FragmentWithArgs
public class WishlistSettingsFragment extends BasePresenterFragment<WishlistSettingsView, WishlistSettingsPresenter>
        implements WishlistSettingsView {

    @Arg
    long wishlistId;

    @Bind(R.id.settings_wishlist_name)
    EditText wlNameEditText;

    @Bind(R.id.settings_wishlist_occasion)
    Spinner wlOccasionSpinner;

    @Bind(R.id.button_start_product_picker_settings_activity)
    Button startProductPickerSettingsButton;

    @Inject
    IntentStarter intentStarter;

    String occasionSelected;

    @OnItemSelected(R.id.settings_wishlist_occasion)
    public void onItemSelected(int position) {
        occasionSelected = String.valueOf(wlOccasionSpinner.getSelectedItem());
    }

    @OnClick(R.id.button_start_product_picker_settings_activity)
    public void startProductPickerSettings(){

        String wlName = wlNameEditText.getText().toString();
        if (wlName == "") {
            wlName = "Unnamed wl";
        }

        //If it is 0, it's a wl created from scratch, otherwise I need the id to load previous settings
        if(wishlistId == 0) {
            Timber.d("new wl");
            Random random = new Random();
            wishlistId = Math.abs(random.nextLong());
        }

        getPresenter().onWishlistAdded(new Wishlist(wishlistId, wlName));
        intentStarter.startProductPickerSettingsActivity(getContext(), wishlistId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("id of wishlist is: " + wishlistId);
        setRetainInstance(true);
    }

    @Override
    public WishlistSettingsPresenter createPresenter() {
        return this.getComponent(WishlistSettingsComponent.class).providePresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wishlist_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTextBoxes();
    }

    protected void setTextBoxes(){
        if (wishlistId==0) {
            Timber.d("new wl");
        } else {
            Wishlist currentWishlist = getPresenter().onWishlistSettingsLoaded(wishlistId);
            wlNameEditText.setText(currentWishlist.getName());
            //wlOccasionSpinner.setSelection(((ArrayAdapter)(wlOccasionSpinner.getAdapter())).getPosition(currentWishlist.getOccasion()));

        }
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(WishlistSettingsComponent.class).inject(this);
    }

    @Override
    public void showWishlistAddedError() {
        Timber.d("error in adding wl");
    }

    @Override
    public void showWishlistAddedSuccess() {
        Timber.d("success in adding wl");
    }
}
