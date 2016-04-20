package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.domain.interactor.GetProductListUseCase;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductListAdapter;

/**
 * Created by Elena on 27/01/2016.
 */
@Module()
public class ProductListModule {

    private static final String EMPTY_STRING = "";

    //keywords and category has to be initialized by default to something since they are used in the @Provides, otherwise Dagger won't build
    private String keywords = EMPTY_STRING;
    private String category = EMPTY_STRING;
    private Map<Class, Boolean> enabledRepositoryMap = new HashMap<Class, Boolean>();
    private Context context;

    public ProductListModule(Context context, Map<Class, Boolean> enabledRepositoryMap, String category) {
        this.context = context;
        this.enabledRepositoryMap = enabledRepositoryMap;
        this.category = category;
    }

    public ProductListModule(Context context, Map<Class, Boolean> enabledRepositoryMap, String category, String keywords) {
        this.context = context;
        this.enabledRepositoryMap = enabledRepositoryMap;
        this.category = category;
        this.keywords = keywords;
    }

    @Provides
    @PerActivity
    GetProductListUseCase provideGetProductListUseCase(List<ProductRepository<Product>> productRepositoryList,
                                                       CurrencyRepository currencyRepository,
                                                       EventBus eventBus) {
        return new GetProductListUseCase(productRepositoryList, currencyRepository, category, keywords, eventBus);
    }

    //Edit this method to add new product data sources
    @Provides
    @PerActivity
    List<ProductRepository<Product>> provideProductRepositoryList(@Named("EbayRepository") ProductRepository<Product> ebayProductDataSource,
                                                                  @Named("EtsyRepository")ProductRepository<Product> etsyProductDataSource) {
        List<ProductRepository<Product>> productRepositoryList = new ArrayList<>();
        if (enabledRepositoryMap.get(EbayProductDataSource.class)) {
            productRepositoryList.add(ebayProductDataSource);
        }
        if (enabledRepositoryMap.get(EtsyProductDataSource.class)) {
            productRepositoryList.add(etsyProductDataSource);
        }
        return productRepositoryList;
    }

    @Provides
    @PerActivity
    Picasso providesPicasso() {
        return Picasso.with(context);
    }

    @Provides
    @PerActivity
    ProductListAdapter providesProductListAdapter(Picasso picasso) {
        return new ProductListAdapter(context, picasso);
    }
}
