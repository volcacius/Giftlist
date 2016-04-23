package it.polimi.dima.giftlist.presentation.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.event.AdapterEmptyEvent;
import it.polimi.dima.giftlist.presentation.view.ProductListView;
import it.polimi.dima.giftlist.domain.interactor.GetProductListUseCase;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductListPresenter extends BaseRxLcePresenter<ProductListView, List<Product>, GetProductListUseCase> {

    private static final boolean NO_PULL_TO_REFRESH = false;
    private boolean isSubscriptionPending;

    @Inject
    public ProductListPresenter(EventBus eventBus, GetProductListUseCase getProductListUseCase) {
        super(eventBus, getProductListUseCase);
        this.isSubscriptionPending = false;
    }

    /**
     * Subscribes the presenter himself as subscriber on the observable
     * This pattern of calling onCompleted, onError and onNext of the Presenter from the inner subscriber class
     * allows, if necessary, to override them from classes that extends the Presenter while leaving the subscriber untouched.
     * To get an idea, see e.g. https://ideone.com/mIeavZ
     *
     * ShowLoading is not called here, otherwise it would show the loading view when subscribing in background, even if the
     * is still stuff in the adapter. ShowLoading is called only as an empty view of the adapter
     *
     * @param pullToRefresh Pull to refresh?
     */
    @Override
    @DebugLog
    public void subscribe(boolean pullToRefresh) {
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    @Override
    protected void onCompleted() {
        if (isViewAttached()) {
            getView().showContent();
        }
        unsubscribe();
        checkPendingSubscription();
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
        checkPendingSubscription();
    }

    @Override
    protected void onNext(List<Product> data) {
        if (isViewAttached()) {
            getView().appendData(data);
            getView().showContent();
        }
    }

    //This is way to manage messages back from the view layer. When the adapter is emptying,
    //it sends a message to the presenter to remind it to load more data. If a subscription isn't ongoing,
    //subscribe. Otherwise, if a subscription is already open and there is no pending future subscription,
    //set a subscription as pending.
    @DebugLog
    @Subscribe
    public void onAdapterEmptyEvent(AdapterEmptyEvent event) {
        if (useCase.isUnsubscribed()) {
            subscribe(NO_PULL_TO_REFRESH);
        } else if (!isSubscriptionPending) {
            isSubscriptionPending = true;
        }
    }

    //Check if there is a pending subscription to register
    private void checkPendingSubscription() {
        if (isSubscriptionPending) {
            subscribe(NO_PULL_TO_REFRESH);
            isSubscriptionPending = false;
        }
    }
}


