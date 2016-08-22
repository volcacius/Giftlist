package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.presentation.component.ProductPickerSettingsComponent;
import it.polimi.dima.giftlist.presentation.component.WishlistSettingsComponent;
import it.polimi.dima.giftlist.presentation.presenter.ProductPickerSettingsPresenter;
import it.polimi.dima.giftlist.presentation.view.ProductPickerSettingsView;
import it.polimi.dima.giftlist.util.CategoryDeterminer;
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

    @Bind(R.id.button_start_product_activity)
    Button startProductActivityButton;

    @Bind(R.id.text_maxprice)
    EditText maxpriceEditText;

    @Bind(R.id.text_minprice)
    EditText minpriceEditText;

    @Bind(R.id.select_age)
    Spinner ageSpinner;
    String ageSelected;

    @Bind(R.id.text_keywords)
    EditText keywordsEditText;

    @Bind(R.id.button_more_options)
    Button moreButton;

    HashMap<Class,Boolean> enabledRepositoryMap;
    ArrayList<String> chosenCategoriesList;

    @OnItemSelected(R.id.select_age)
    public void onItemSelected(int position) {
        ageSelected = String.valueOf(ageSpinner.getSelectedItem());
    }

    @OnClick(R.id.button_more_options)
    public void revealMoreOptions() {
        if(ageSpinner.getVisibility() == View.GONE) {
            ageSpinner.setVisibility(View.VISIBLE);
        } else {
            keywordsEditText.setVisibility(View.VISIBLE);
            moreButton.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button_start_product_activity)
    public void startProductActivity(){
        Float minprice;
        Float maxprice;
        try
        {
            minprice =  Float.parseFloat(minpriceEditText.getText().toString());
        }
        catch(NumberFormatException e)
        {
            minprice = DEFAULT_MIN;
        }

        try
        {
            maxprice  = Float.parseFloat(maxpriceEditText.getText().toString());
        }
        catch(NumberFormatException e)
        {
            maxprice = DEFAULT_MAX;
        }

        intentStarter.startProductPickerActivity(this.getContext(),
                                                enabledRepositoryMap,
                                                chosenCategoriesList,
                                                EMPTY_STRING,//TODO remove later
                                                keywordsEditText.getText().toString(),
                                                maxprice,
                                                minprice,
                                                wishlistId);
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
        String occasion = getPresenter().getWishlist(wishlistId).getOccasion();
        chosenCategoriesList = CategoryDeterminer.getCategoriesFromOccasion(occasion);
        chosenCategoriesList.addAll(CategoryDeterminer.getCategoriesFromAge(ageSelected));
        Timber.d(occasion);
        if (!chosenCategoriesList.isEmpty()) {
            Timber.d("recommended categories " + chosenCategoriesList.get(0));
            keywordsEditText.setVisibility(View.GONE);
            ageSpinner.setVisibility(View.GONE);
        } else {
            Timber.d("no recommended categories ");
            moreButton.setVisibility(View.GONE);
        }

        //for now set them true by default
        enabledRepositoryMap = new HashMap<>();
        enabledRepositoryMap.put(EbayProductDataSource.class, Boolean.TRUE);
        enabledRepositoryMap.put(EtsyProductDataSource.class, Boolean.TRUE);

    }


    @Override
    protected void injectDependencies() {
        this.getComponent(ProductPickerSettingsComponent.class).inject(this);
    }

}
