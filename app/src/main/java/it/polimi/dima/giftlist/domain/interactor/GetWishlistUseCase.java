package it.polimi.dima.giftlist.domain.interactor;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import rx.Observable;

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetWishlistUseCase extends UseCase<Wishlist> {

    @Inject
    public GetWishlistUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<Wishlist> buildUseCaseObservable() {
        return null;
    }
}