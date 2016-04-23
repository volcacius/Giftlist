package it.polimi.dima.giftlist.presentation.view.fragment;

/**
 * Created by Elena on 08/01/2016.
 */
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import icepick.Icepick;
import it.polimi.dima.giftlist.GiftlistApplication;
import it.polimi.dima.giftlist.di.HasComponent;

/**
 * Base LCE ViewState Fragment for this app that uses Butterknife, Icepick and dependency injection
 *
 */
public abstract class BaseViewStateLceFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceViewStateFragment<CV, M, V, P> {

    @Inject
    EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    // Not needed in general. override it in subclass if you need this callback
    @Override
    public void onViewStateInstanceRestored(boolean instanceStateRetained) {
        super.onViewStateInstanceRestored(instanceStateRetained);
    }

    @Override
    public void onNewViewStateInstance() {
        loadData(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = GiftlistApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    /**
     * Inject the dependencies, has to be overwritten
     */
    protected void injectDependencies() {
    }

    /**
     * Gets a component for dependency injection by its type.
     * This pattern allows to avoid ugly casting in each fragment
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}