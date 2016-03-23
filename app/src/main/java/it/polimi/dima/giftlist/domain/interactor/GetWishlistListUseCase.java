package it.polimi.dima.giftlist.domain.interactor;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Wishlist;
import rx.Observable;

/**
 * Created by Alessandro on 18/03/16.
 */
public class GetWishlistListUseCase extends UseCase<List<Wishlist>> {

    @Override
    protected Observable<List<Wishlist>> buildUseCaseObservable() {
        return null;
    }
}
