package it.polimi.dima.giftlist.presentation.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CategoryType;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.event.AdapterAboutToEmptyEvent;
import it.polimi.dima.giftlist.presentation.event.ProductAddedEvent;
import it.polimi.dima.giftlist.presentation.exception.UnknownProductException;
import it.polimi.dima.giftlist.presentation.navigation.IntentStarter;
import it.polimi.dima.giftlist.presentation.view.ProductPickerView;
import it.polimi.dima.giftlist.presentation.component.ProductPickerComponent;
import it.polimi.dima.giftlist.presentation.presenter.ProductPickerPresenter;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductPickerAdapter;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;
import it.polimi.dima.giftlist.util.ToastFactory;
import timber.log.Timber;

/**
 * Created by Elena on 19/01/2016.
 */
@FragmentWithArgs
public class ProductPickerFragment extends BaseMvpLceFragment<SwipeFlingAdapterView, List<Product>, ProductPickerView, ProductPickerPresenter>
        implements ProductPickerView {

    private static final boolean NO_PULL_TO_REFRESH = false;

    @Bind(R.id.contentView)
    SwipeFlingAdapterView flingContainer;
    @Bind(R.id.like_button)
    LikeButton likeButton;
    @Bind(R.id.discard_button)
    LikeButton discardButton;
    @Bind(R.id.reload_button)
    Button reloadButton;


    @Arg
    long wishlistId;
    @Arg
    HashMap<Class, Boolean> enabledProductRepositoryMap;
    @Arg
    ArrayList<CategoryType> chosenCategoriesList;
    @Arg
    String keywords;

    @Inject
    ProductPickerAdapter productPickerAdapter;
    @Inject
    IntentStarter intentStarter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product_picker;
    }

    @Override
    protected void injectDependencies() {
        this.getComponent(ProductPickerComponent.class).inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flingContainer.setAdapter(productPickerAdapter);
        flingContainer.setEmptyView(loadingView);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                productPickerAdapter.removeFirstProduct();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Product p = (Product) o;
                String root = getContext().getExternalCacheDir().toString();
                String name = p.getId() + "_pic.jpg";
                String uri = root + name;
                p.setImageUri(uri);
                eventBus.post(new ProductAddedEvent(p));
                likeButton.setLiked(false);

            }

            @Override
            public void onRightCardExit(Object o) {
                discardButton.setLiked(false);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                eventBus.post(new AdapterAboutToEmptyEvent());
            }

            @Override
            public void onScroll(float v) {

            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                flingContainer.getTopCardListener().selectLeft();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        discardButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                flingContainer.getTopCardListener().selectRight();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
    }

    @OnClick(R.id.reload_button)
    public void reloadItems() {
        loadData(NO_PULL_TO_REFRESH);
    }

    @NonNull
    @Override
    public ProductPickerPresenter createPresenter() {
        return this.getComponent(ProductPickerComponent.class).provideProductListPresenter();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @NonNull
    @Override
    public LceViewState<List<Product>, ProductPickerView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @DebugLog
    @Override
    public List<Product> getData() {
        return productPickerAdapter.getProductList();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorMessageDeterminer.getErrorMessage(e);
    }

    @DebugLog
    @Override
    public void appendData(List<Product> data) {
        productPickerAdapter.appendProductList(data);
    }

    @DebugLog
    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.subscribe(NO_PULL_TO_REFRESH);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, NO_PULL_TO_REFRESH);
        reloadButton.setVisibility(View.VISIBLE);
        reloadButton.setEnabled(true);
        e.printStackTrace();
    }

    @Override
    public void setData(List<Product> data) {
        productPickerAdapter.setProductList(data);
    }

    @Override
    public void showContent() {
        super.showContent();
        likeButton.setEnabled(true);
        discardButton.setEnabled(true);
        reloadButton.setVisibility(View.GONE);
        reloadButton.setEnabled(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        likeButton.setEnabled(false);
        discardButton.setEnabled(false);
        super.showLoading(NO_PULL_TO_REFRESH);
    }

    @Override
    public void showProductAddedError() {
        ToastFactory.showShortToast(getContext(), R.string.product_added);
    }

    @Override
    public void showProductAddedSuccess() {
        ToastFactory.showShortToast(getContext(), R.string.product_added_error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wishlist:
                Timber.d("button add pressed");
                intentStarter.startWishlistActivity(getContext(), wishlistId);
                return true;

            case R.id.action_settings:
                Timber.d("button settings pressed");
                intentStarter.startWishlistSettingsActivity(getContext(), wishlistId);
                return true;

            default:
                break;

        }
        return false;
    }
}
