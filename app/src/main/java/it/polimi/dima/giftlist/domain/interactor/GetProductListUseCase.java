package it.polimi.dima.giftlist.domain.interactor;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.repository.ItemRepository;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public class GetProductListUseCase implements UseCase<List<EtsyProduct>> {

    private final ItemRepository mItemRepository;
    private int currentOffset;

    @Inject
    public GetProductListUseCase(ItemRepository itemRepository) {
        mItemRepository = itemRepository;
        currentOffset = -1;
    }

    @Override
    public Observable<List<EtsyProduct>> execute() {
       /* currentOffset = currentOffset + 1;
        return mItemRepository.getItems(currentOffset*25)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());*/
        return null;
    }

    public Observable<List<EtsyProduct>> execute(String category, String keywords) {
        currentOffset = currentOffset + 1;
        return mItemRepository.getItems(category, keywords, currentOffset*25);
               // .subscribeOn(Schedulers.newThread())
                // .observeOn(AndroidSchedulers.mainThread());
    }
}
