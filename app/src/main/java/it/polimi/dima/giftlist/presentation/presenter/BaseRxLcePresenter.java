package it.polimi.dima.giftlist.presentation.presenter;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import org.greenrobot.eventbus.EventBus;

import it.polimi.dima.giftlist.domain.interactor.UseCase;
import rx.Subscriber;

/**
 * Created by Alessandro on 08/01/16.
 */
public abstract class BaseRxLcePresenter<V extends MvpLceView<M>, M, U extends UseCase<M>>
        extends com.hannesdorfmann.mosby.mvp.MvpBasePresenter<V>
        implements com.hannesdorfmann.mosby.mvp.MvpPresenter<V> {

    protected EventBus eventBus;
    protected U useCase;

    public BaseRxLcePresenter(EventBus eventBus, U useCase) {
        this.eventBus = eventBus;
        this.useCase = useCase;
    }

    /**
     * Unsubscribes
     */
    protected void unsubscribe() {
        useCase.unsubscribe();
    }

    /**
     * Subscribes the presenter himself as subscriber on the observable
     *
     * @param pullToRefresh Pull to refresh?
     */
    public void subscribe(final boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }
        unsubscribe();
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    protected void onCompleted() {
        if (isViewAttached()) {
            getView().showContent();
        }
        unsubscribe();
    }

    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    protected void onNext(M data) {
        if (isViewAttached()) {
            getView().setData(data);
        }
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        eventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            unsubscribe();
        }
        eventBus.unregister(this);
    }

    private final class BaseSubscriber extends Subscriber<M> {

        private boolean pullToRefresh;

        public BaseSubscriber(boolean pullToRefresh) {
            this.pullToRefresh = pullToRefresh;
        }

        @Override
        public void onCompleted() {
            BaseRxLcePresenter.this.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            BaseRxLcePresenter.this.onError(e, pullToRefresh);
        }

        @Override
        public void onNext(M m) {
            BaseRxLcePresenter.this.onNext(m);
        }
    }
}
