package it.polimi.dima.giftlist.presentation.presenter;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import it.polimi.dima.giftlist.domain.interactor.UseCase;
import rx.Subscriber;

/**
 * Created by Alessandro on 08/01/16.
 * One of the responsability of the presenter is to update to view it manages, if it is attached,
 * when some ements arrive from the bus, i.e. not the traditional data flow from the use cases,
 * to which it is subscribed through rx, but side effects of stuff happening in other UIs it does not manage.
 * Events vs Rx is like UDP versus TCP: events does not require to manage a subscription,
 * which would be more complicated since subscribing to activities and fragments mean having to keep up
 * with their lifecycles.
 */
public abstract class BaseRxLcePresenter<V extends MvpLceView<M>, M, U extends UseCase<M>>
        extends com.hannesdorfmann.mosby.mvp.MvpBasePresenter<V>
        implements com.hannesdorfmann.mosby.mvp.MvpPresenter<V> {

    protected EventBus eventBus;
    protected U useCase;
    protected StorIOSQLite db;

    public BaseRxLcePresenter(EventBus eventBus, U useCase, StorIOSQLite db) {
        this.eventBus = eventBus;
        this.useCase = useCase;
        this.db = db;
    }

    /**
     * Unsubscribes
     */
    protected void unsubscribe() {
        useCase.unsubscribe();
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
    public abstract void subscribe(boolean pullToRefresh);

    abstract protected void onCompleted();

    abstract protected void onError(Throwable e, boolean pullToRefresh);

    abstract protected void onNext(M data);


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

    final class BaseSubscriber extends Subscriber<M> {

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
