package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import icepick.State;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CategoryType;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.presentation.component.ProductPickerSettingsComponent;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.presenter.ProductPickerSettingsPresenter;
import it.polimi.dima.giftlist.presentation.view.ProductPickerSettingsView;
import timber.log.Timber;

/**
 * Created by Elena on 10/02/2016.
 */
@FragmentWithArgs
public class ProductPickerSettingsFragment extends BaseMvpFragment<ProductPickerSettingsView, ProductPickerSettingsPresenter> {

    private static final String EMPTY_STRING = "";
    private static final Float DEFAULT_MAX = (float) 1000.0;
    private static final Float DEFAULT_MIN = (float) 0.0;

    @Arg
    long wishlistId;

    @State
    int startingDisplayOrder;

    @Bind(R.id.button_start_product_activity)
    Button startProductActivityButton;
    @Bind(R.id.text_maxprice)
    EditText maxpriceEditText;
    @Bind(R.id.text_minprice)
    EditText minpriceEditText;
    @Bind(R.id.select_age)
    Spinner ageSpinner;
    @Bind(R.id.text_keywords)
    EditText keywordsEditText;
    @Bind(R.id.button_more_options)
    Button moreButton;
    @Bind(R.id.checkbox_art)
    CheckBox artCheckbox;
    @Bind(R.id.checkbox_games)
    CheckBox gamesCheckbox;
    @Bind(R.id.checkbox_sports)
    CheckBox sportsCheckbox;
    @Bind(R.id.checkbox_technology)
    CheckBox techCheckbox;
    @Bind(R.id.checkbox_travelling)
    CheckBox travelCheckbox;
    @Bind(R.id.checkbox_handcraft)
    CheckBox handcraftCheckbox;
    @Bind(R.id.checkbox_nerd)
    CheckBox nerdCheckbox;
    @Bind(R.id.checkbox_books)
    CheckBox bookCheckbox;
    @Bind(R.id.checkbox_music)
    CheckBox musicCheckbox;
    @Bind(R.id.category_checkboxes)
    LinearLayout categoryCheckboxes;

    HashMap<Class,Boolean> enabledRepositoryMap;
    ArrayList<CategoryType> chosenCategoriesList;
    String ageSelected;

    @OnItemSelected(R.id.select_age)
    public void onItemSelected(int position) {
        ageSelected = String.valueOf(ageSpinner.getSelectedItem());
    }

    @OnClick(R.id.button_more_options)
    public void revealMoreOptions() {
        if ((ageSpinner.getVisibility() == View.GONE) && (checkOccasionForAge())) {
            ageSpinner.setVisibility(View.VISIBLE);
        } else if (keywordsEditText.getVisibility() == View.GONE){
            keywordsEditText.setVisibility(View.VISIBLE);
        } else {
            categoryCheckboxes.setVisibility(View.VISIBLE);
            moreButton.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button_start_product_activity)
    public void startProductActivity() {
        chosenCategoriesList = getChosenCategoriesFromUI();
        Float minprice;
        Float maxprice;
        try {
            minprice =  Float.parseFloat(minpriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            minprice = DEFAULT_MIN;
        } try {
            maxprice  = Float.parseFloat(maxpriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            maxprice = DEFAULT_MAX;
        }
        IntentStarter.startProductPickerActivity(this.getContext(),
                                                enabledRepositoryMap,
                                                chosenCategoriesList,
                                                keywordsEditText.getText().toString(),
                                                maxprice,
                                                minprice,
                                                wishlistId,
                                                startingDisplayOrder);
    }

    private ArrayList<CategoryType> getChosenCategoriesFromUI() {
        chosenCategoriesList = new ArrayList<>();
        String occasion = getPresenter().getWishlist(wishlistId).getOccasion();
        chosenCategoriesList.add(CategoryType.getCategoryTypeFromString(occasion));
        if (ageSpinner.getVisibility()==View.VISIBLE) {
            Timber.d("adding "+ageSelected);
            chosenCategoriesList.add(CategoryType.getCategoryTypeFromString(ageSelected));
        }

        if (categoryCheckboxes.getVisibility() == View.VISIBLE) {
            if (gamesCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.GAME);
            }
            if (handcraftCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.HANDCRAFT);
            }
            if (techCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.TECH);
            }
            if (sportsCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.SPORT);
            }
            if (travelCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.TRAVEL);
            }
            if (artCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.ART);
            }
            if (nerdCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.NERD);
            }
            if (bookCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.BOOK);
            }
            if (musicCheckbox.isChecked()) {
                chosenCategoriesList.add(CategoryType.MUSIC);
            }
        }

        return chosenCategoriesList;
    }

    private boolean checkOccasionForAge(){
        List<String> ageOccasions = new ArrayList<>();
        ageOccasions.addAll(Arrays.asList("wedding","christening","graduation","engagement","anniversary"));
        if (ageOccasions.contains(getPresenter().getWishlist(wishlistId).getOccasion())) {
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product_picker_settings;
    }

    @Override
    public ProductPickerSettingsPresenter createPresenter() {
        return this.getComponent(ProductPickerSettingsComponent.class).providePresenter();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        keywordsEditText.setText(EMPTY_STRING);

        //for now set them true by default
        enabledRepositoryMap = new HashMap<>();
        enabledRepositoryMap.put(EbayProductDataSource.class, Boolean.TRUE);
        enabledRepositoryMap.put(EtsyProductDataSource.class, Boolean.TRUE);

        startingDisplayOrder = presenter.getStartingProductDisplayOrder(wishlistId);
        Timber.d("Starting product display order is %d", startingDisplayOrder);
    }


    @Override
    protected void injectDependencies() {
        this.getComponent(ProductPickerSettingsComponent.class).inject(this);
    }

}
