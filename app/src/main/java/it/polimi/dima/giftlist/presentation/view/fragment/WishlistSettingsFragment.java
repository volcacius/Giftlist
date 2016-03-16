package it.polimi.dima.giftlist.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.view.activity.ProductActivity;

/**
 * Created by Elena on 10/02/2016.
 */
public class WishlistSettingsFragment extends Fragment {

    @Bind(R.id.etsy_categories)
    Spinner mEtsyCategories;
    @Bind(R.id.start_product_activity)
    Button mStartActivityButton;
    @Bind(R.id.keywords)
    EditText mKeywordsEditText;

    String mCategorySelected = "";

    @OnClick(R.id.start_product_activity)
    public void startProductActivity(){
        ProductActivity.start(this.getContext(), mCategorySelected, mKeywordsEditText.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wishlistsettings, container, false); //xml file name
        ButterKnife.bind(this,v);
        mKeywordsEditText.setText("");

        return v;
    }

    @OnItemSelected(R.id.etsy_categories)
    public void onItemSelected(int position) {
        mCategorySelected = String.valueOf(mEtsyCategories.getSelectedItem());
    }

}
