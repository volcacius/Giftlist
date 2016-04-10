package it.polimi.dima.giftlist.presentation.presenter;

import java.util.List;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.event.AdapterAboutToEmptyEvent;
import it.polimi.dima.giftlist.presentation.event.ProductAddedEvent;
import it.polimi.dima.giftlist.presentation.exception.NoMoreResultsFoundException;
import it.polimi.dima.giftlist.presentation.exception.NoResultsFoundException;
import it.polimi.dima.giftlist.presentation.view.ProductListView;
import it.polimi.dima.giftlist.domain.interactor.GetProductListUseCase;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductListPresenter extends BaseRxLcePresenter<ProductListView, List<Product>, GetProductListUseCase> {

    private static final boolean NO_PULL_TO_REFRESH = false;

    @Inject
    public ProductListPresenter(EventBus eventBus, GetProductListUseCase getProductListUseCase) {
        super(eventBus, getProductListUseCase);
    }


    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            if (e instanceof NoResultsFoundException) {
                getView().showNoResultsFound();
            } else if (e instanceof NoMoreResultsFoundException) {
                getView().showNoMoreResultsFound();
            } else {
                getView().showError(e, pullToRefresh);
            }
        }
        unsubscribe();
    }

    @Subscribe
    private void onProductAddedEvent(ProductAddedEvent event) {
        event.getProduct();
    }
}


