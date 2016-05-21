package it.polimi.dima.giftlist.presentation.view.activity;

import android.os.Bundle;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerSettingsFragment;
import it.polimi.dima.giftlist.presentation.view.fragment.ProductPickerSettingsFragmentBuilder;
import it.polimi.dima.giftlist.presentation.view.fragment.WishlistListFragment;

/**
 * Created by Elena on 10/02/2016.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            intentStarter.startProductPickerSettingsActivity(this, 0);
        }
    }
}