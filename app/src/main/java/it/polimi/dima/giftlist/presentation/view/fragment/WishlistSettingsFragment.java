package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;

/**
 * Created by Elena on 10/02/2016.
 */
public class WishlistSettingsFragment extends BaseFragment {

    private static final String EMPTY_STRING = "";

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

    String categorySelected;
    HashMap<Class,Boolean> enabledRepositoryMap;

    @OnClick(R.id.button_start_product_activity)
    public void startProductActivity(){
        intentStarter.startProductListActivity(this.getContext(),
                                                enabledRepositoryMap,
                                                categorySelected,
                                                keywordsEditText.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wishlistsettings;
    }

    /*
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wishlistsettings, container, false); //xml file name
        ButterKnife.bind(this,v);
        //TODO questo override è da eliminare, trovare un modo più pulito per settare il valore di default della view
        keywordsEditText.setText("");
        return v;
    }
    */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keywordsEditText.setText(EMPTY_STRING);
        categorySelected = EMPTY_STRING;
        enabledRepositoryMap = new HashMap<>();
        enabledRepositoryMap.put(EbayProductDataSource.class, Boolean.FALSE);
        enabledRepositoryMap.put(EtsyProductDataSource.class, Boolean.FALSE);
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
