package it.polimi.dima.giftlist.product;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import it.polimi.dima.giftlist.base.BaseRxLcePresenter;
import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.product.Rest.GetProductListUseCase;
import it.polimi.dima.giftlist.product.converter.CurrencyDownloader;
import it.polimi.dima.giftlist.product.converter.RetrofitEvent;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductPresenter extends BaseRxLcePresenter<ProductView, List<EtsyProduct>> {

    private final GetProductListUseCase mProductListUseCase;
    EventBus mEventBus;

    @Inject
    public ProductPresenter(GetProductListUseCase productListUseCase) {
        mProductListUseCase = productListUseCase;
        mEventBus = EventBus.getDefault();

    }

    public void onEvent(RetrofitEvent event) {
        getView().loadData(true);
    }


    @Override
    public void attachView(ProductView view) {
        super.attachView(view);
        mEventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        mEventBus.unregister(this);
    }


    public void loadRetrofit(String category, boolean pullToRefresh) {
        subscribe(mProductListUseCase.execute(category,""), pullToRefresh);
    }

    public void loadRetrofit(String category, String keywords, boolean pullToRefresh) {
        subscribe(mProductListUseCase.execute(category, keywords), pullToRefresh);
    }



}


