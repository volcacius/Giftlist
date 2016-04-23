package it.polimi.dima.giftlist.domain.interactor;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Elena on 27/01/2016.
 */
public abstract class UseCase<T> {

    private Subscription subscription = Subscriptions.empty();

    protected abstract Observable<T> buildUseCaseObservable();

    public void execute(Subscriber<T> subscriber) {
        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public boolean isUnsubscribed() {
        return subscription.isUnsubscribed();
    }
}
