package it.polimi.dima.giftlist.presentation.presenter;

import java.util.List;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.view.ProductListView;
import it.polimi.dima.giftlist.domain.interactor.GetProductListUseCase;
import it.polimi.dima.giftlist.data.event.RetrofitEvent;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductListPresenter extends BaseRxLcePresenter<ProductListView, List<Product>, GetProductListUseCase> {

    @Inject
    public ProductListPresenter(EventBus eventBus, GetProductListUseCase getProductListUseCase) {
        super(eventBus, getProductListUseCase);
    }

    @Subscribe
    public void onEvent(RetrofitEvent event) {
        getView().loadData(true);
    }

}


