package it.polimi.dima.giftlist.domain.interactor;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Wishlist;
import rx.Observable;

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetWishlistUseCase extends UseCase<Wishlist> {

    @Override
    protected Observable<Wishlist> buildUseCaseObservable() {
        return null;
    }
}
