package it.polimi.dima.giftlist.presentation.presenter;

import java.util.List;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import it.polimi.dima.giftlist.base.BaseRxLcePresenter;
import it.polimi.dima.giftlist.presentation.model.EtsyProduct;
import it.polimi.dima.giftlist.presentation.view.ProductView;
import it.polimi.dima.giftlist.domain.interactor.GetProductListUseCase;
import it.polimi.dima.giftlist.domain.event.RetrofitEvent;

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


