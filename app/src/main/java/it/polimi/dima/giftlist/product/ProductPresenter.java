package it.polimi.dima.giftlist.product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.base.BaseRxLcePresenter;
import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.product.Rest.GetProductListUseCase;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductPresenter extends BaseRxLcePresenter<ProductView, List<EtsyProduct>> {

    private final GetProductListUseCase mProductListUseCase;


    @Inject
    public ProductPresenter(GetProductListUseCase productListUseCase) {
        mProductListUseCase = productListUseCase;
    }


    @Override
    public void attachView(ProductView view) {
        super.attachView(view);
        //eventBus.register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }


    public void loadRetrofit(String category, boolean pullToRefresh) {
        subscribe(mProductListUseCase.execute(category,""), pullToRefresh);
    }

    public void loadRetrofit(String category, String keywords, boolean pullToRefresh) {
        subscribe(mProductListUseCase.execute(category, keywords), pullToRefresh);
    }



}


