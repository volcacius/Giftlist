package it.polimi.dima.giftlist.domain.interactor;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.repository.ProductRepository;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import rx.Observable;

/**
 * Created by Elena on 27/01/2016.
 */
public class GetProductListUseCase extends UseCase<List<Product>> {

    private final int DEFAULT_OFFSET = 25;
    private final ProductRepository productRepository;
    private final String category;
    private final String keywords;

    @Inject
    public GetProductListUseCase(String category,
                                 String keywords,
                                 ProductRepository productRepository,
                                 ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
        this.category = category;
        this.keywords = keywords;
    }

    @Override
    protected Observable<List<Product>> buildUseCaseObservable() {
        return this.productRepository.getProductList(category, keywords, DEFAULT_OFFSET);
    }
}
