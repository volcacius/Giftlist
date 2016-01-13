package it.polimi.dima.giftlist.wishlistlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.FirstModule;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.base.BaseViewStateLceFragment;

/**
 * Created by Alessandro on 08/01/16.
 */
public class WishlistListFragment extends BaseViewStateLceFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.fragment_whislist_list_recycler_view) RecyclerView mRecyclerView;
    @Inject WishlistListAdapter mWishlistListAdapter;
    WishlistListComponent mWishlistListComponent;

/*
    protected void injectDependencies() {
        mWishlistListComponent =
                DaggerWishlistListComponent
        mWishlistListComponent.inject(this);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWishlistListComponent = Dagger_WishlistListComponent.builder().firstModule(new FirstModule(getActivity())).build();
        setRetainInstance(true);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //injectDependencies();     <--TODO
        //ButterKnife is done in onViewCreate, and also setLayoutManager
        return inflater.inflate(R.layout.fragment_whislistlist, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mWishlistListAdapter = mWishlistListComponent.adapter();//CAPIRE CHE SONO I COMPONENTS
        mRecyclerView.setAdapter(mWishlistListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.setOnRefreshListener(this);
        //createMailButton.attachToRecyclerView(recyclerView);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_whislistlist;
    }

    @Override
    public LceViewState createViewState() {
        return null;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public MvpPresenter createPresenter() {
        return null;
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }

    @Override
    public void onRefresh() {

    }
}
