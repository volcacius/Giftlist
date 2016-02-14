package it.polimi.dima.giftlist.product.Rest;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.base.Repository;
import it.polimi.dima.giftlist.base.UseCase;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Elena on 27/01/2016.
 */
public class GetProductListUseCase implements UseCase<List<EtsyProduct>> {

    private final Repository mRepository;
    private int currentOffset;

    @Inject
    public GetProductListUseCase(Repository repository) {
        mRepository = repository;
        currentOffset = -1;
    }

    @Override
    public Observable<List<EtsyProduct>> execute() {
       /* currentOffset = currentOffset + 1;
        return mRepository.getItems(currentOffset*25)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());*/
        return null;
    }

    public Observable<List<EtsyProduct>> execute(String category, String keywords) {
        currentOffset = currentOffset + 1;
        return mRepository.getItems(category, keywords, currentOffset*25)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
