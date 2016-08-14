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

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;

/**
 * Created by Elena on 10/02/2016.
 */
@FragmentWithArgs
public class ProductPickerSettingsFragment extends BaseFragment {

    private static final String EMPTY_STRING = "";
    private static final Float DEFAULT_MAX = (float) 1000.0;
    private static final Float DEFAULT_MIN = (float) 0.0;

    @Arg
    long wishlistId;

    @Bind(R.id.checkbox_ebay)
    CheckBox ebayCheckbox;

    @Bind(R.id.checkbox_etsy)
    CheckBox etsyCheckbox;

    @Bind(R.id.spinner_category)
    Spinner categorySpinner;

    @Bind(R.id.button_start_product_activity)
    Button startProductActivityButton;

    @Bind(R.id.text_keywords)
    EditText keywordsEditText;

    @Bind(R.id.text_maxprice)
    EditText maxpriceEditText;

    @Bind(R.id.text_minprice)
    EditText minpriceEditText;

    String categorySelected;
    HashMap<Class,Boolean> enabledRepositoryMap;

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
                                                categorySelected,
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keywordsEditText.setText(EMPTY_STRING);
        categorySelected = EMPTY_STRING;
        enabledRepositoryMap = new HashMap<>();
        enabledRepositoryMap.put(EbayProductDataSource.class, Boolean.FALSE);
        enabledRepositoryMap.put(EtsyProductDataSource.class, Boolean.FALSE);
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(ApplicationComponent.class).inject(this);
    }

    @OnItemSelected(R.id.spinner_category)
    public void onItemSelected(int position) {
        categorySelected = String.valueOf(categorySpinner.getSelectedItem());
    }

    @OnClick(R.id.checkbox_ebay)
    public void onCheckBoxEbay(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        enabledRepositoryMap.put(EbayProductDataSource.class, checked);
    }

    @OnClick(R.id.checkbox_etsy)
    public void onCheckBoxEtsy(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        enabledRepositoryMap.put(EtsyProductDataSource.class, checked);
    }

}
