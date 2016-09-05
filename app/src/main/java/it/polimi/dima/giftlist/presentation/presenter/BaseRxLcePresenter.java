package it.polimi.dima.giftlist.presentation.presenter;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.UseCase;
import rx.Subscriber;
import timber.log.Timber;

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
        extends com.hannesdorfmann.mosby.mvp.MvpBasePresenter<V> {

    protected U useCase;
    protected StorIOSQLite db;

    public BaseRxLcePresenter(U useCase, StorIOSQLite db) {
        this.useCase = useCase;
        this.db = db;
    }

    /**
     * Unsubscribes
     */
    protected void unsubscribe() {
        Timber.d("Presenter unsubscribes from usecase");
        useCase.unsubscribe();
    }

    /**
     * Subscribes the presenter himself as subscriber on the observable
     * This pattern of calling onCompleted, onError and onNext of the Presenter from the inner subscriber class
     * allows, if necessary, to override them from classes that extends the Presenter while leaving the subscriber untouched.
     * To get an idea, see e.g. https://ideone.com/mIeavZ
     *
     * @param pullToRefresh Pull to refresh?
     */
    public abstract void subscribe(boolean pullToRefresh);

    abstract protected void onCompleted();

    abstract protected void onError(Throwable e, boolean pullToRefresh);

    abstract protected void onNext(M data);

    /*
         * This gets automatically called by Mosby
         */
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            unsubscribe();
        }
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
