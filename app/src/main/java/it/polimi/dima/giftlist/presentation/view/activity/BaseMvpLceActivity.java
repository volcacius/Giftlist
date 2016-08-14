package it.polimi.dima.giftlist.presentation.view.activity;

/**
 * Created by Alessandro on 08/01/16.
 */

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateActivity;
import icepick.Icepick;
import it.polimi.dima.giftlist.ApplicationComponent;
import it.polimi.dima.giftlist.GiftlistApplication;


public abstract class BaseMvpLceActivity<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceViewStateActivity<CV, M, V, P> {

    /* Since most activities do not inject stuff, do not put either an abstract injectDependencies()
       nor injected fields, leave it to subclasses */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((GiftlistApplication) getApplication()).getApplicationComponent();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

}