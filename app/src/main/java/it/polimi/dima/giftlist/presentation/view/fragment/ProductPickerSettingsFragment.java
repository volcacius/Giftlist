package it.polimi.dima.giftlist.presentation.view.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.appyvet.rangebar.RangeBar;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.redbooth.WelcomeCoordinatorLayout;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

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
import it.polimi.dima.giftlist.presentation.view.animation.GiftAnimator;
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

    WelcomeCoordinatorLayout coordinatorLayout;
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
    @Bind(R.id.rangebar)
    RangeBar rangeBar;
    @Bind(R.id.ticker_min)
    TickerView tickerMinView;
    @Bind(R.id.ticker_max)
    TickerView tickerMaxView;

    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;

    HashMap<Class,Boolean> enabledRepositoryMap;
    ArrayList<CategoryType> chosenCategoriesList;
    String ageSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Because of the order of initializations, I can't use Butterknife for coordinaorLayout
        //First: coordinator binding
        //Second: page init
        //Third: butterknife binding
        coordinatorLayout = (WelcomeCoordinatorLayout) view.findViewById(R.id.coordinator);
        initPages();
        super.onViewCreated(view, savedInstanceState);
        initListeners();
        initBackgroundTransitions();
        keywordsEditText.setText(EMPTY_STRING);
        //for now set them true by default
        enabledRepositoryMap = new HashMap<>();
        enabledRepositoryMap.put(EbayProductDataSource.class, Boolean.TRUE);
        enabledRepositoryMap.put(EtsyProductDataSource.class, Boolean.TRUE);
        startingDisplayOrder = presenter.getStartingProductDisplayOrder(wishlistId);

        tickerMinView.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        tickerMaxView.setCharacterList(TickerUtils.getDefaultListForUSCurrency());
        rangeBar.setRangePinsByValue(DEFAULT_MIN, DEFAULT_MAX);
        tickerMinView.setText(String.format("%.1f$", DEFAULT_MIN));
        tickerMaxView.setText(String.format("%.1f$", DEFAULT_MAX));
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                tickerMinView.setText(String.format("%s.0$", leftPinValue));
                tickerMaxView.setText(String.format("%s.0$", rightPinValue));
            }
        });
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
    protected void injectDependencies() {
        this.getComponent(ProductPickerSettingsComponent.class).inject(this);
    }

    @OnItemSelected(R.id.select_age)
    public void onItemSelected(int position) {
        ageSelected = String.valueOf(ageSpinner.getSelectedItem());
    }

    @OnClick(R.id.button_more_options)
    public void revealMoreOptions() {
        if ((ageSpinner.getVisibility() == View.GONE) && (checkOccasionForAge())) {
            ageSpinner.setVisibility(View.VISIBLE);
        } else if (keywordsEditText.getVisibility() == View.GONE) {
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

    private void initBackgroundTransitions() {
        Resources resources = getResources();
        int firstBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_600, getContext().getTheme());
        int secondBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_500, getContext().getTheme());
        int thirdBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_400, getContext().getTheme());
        int fourthBackgroundColor = ResourcesCompat.getColor(resources, R.color.material_purple_300, getContext().getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), firstBackgroundColor, secondBackgroundColor, thirdBackgroundColor, fourthBackgroundColor);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    private void initPages() {
        coordinatorLayout.addPage(R.layout.page_product_picker_settings_first,
                R.layout.page_product_picker_settings_second);
    }

    private void initListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }
            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {
                    case 0:
                        break;
                }
            }
        });
    }
}
