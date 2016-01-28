package it.polimi.dima.giftlist.product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.base.BaseRxLcePresenter;
import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.product.Rest.GetProductListUseCase;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductPresenter extends BaseRxLcePresenter<ProductView, List<EtsyProduct>> {

    private final GetProductListUseCase mProductListUseCase;
    private List<EtsyProduct> mEtsyProducts;

    @Inject
    public ProductPresenter(GetProductListUseCase productListUseCase) {
        mProductListUseCase = productListUseCase;
        mEtsyProducts = new ArrayList<>();
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


    public void loadRetrofit(boolean pullToRefresh) {

        subscribe(mProductListUseCase.execute(), pullToRefresh);

    }



}


