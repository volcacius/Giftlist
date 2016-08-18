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
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.repository.datasource.EbayProductDataSource;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import it.polimi.dima.giftlist.domain.repository.ProductRepository;
import it.polimi.dima.giftlist.domain.interactor.GetNetProductsUseCase;
import it.polimi.dima.giftlist.di.PerActivity;
import it.polimi.dima.giftlist.presentation.view.adapter.ProductPickerAdapter;

/**
 * Created by Elena on 27/01/2016.
 */
@Module()
public class ProductPickerModule {

    private static final String EMPTY_STRING = "";
    private static final Float DEFAULT_MAX_PRICE = (float) 1000.00;
    private static final Float DEFAULT_MIN_PRICE = (float) 0.00;

    //keywords and category has to be initialized by default to something since they are used in the @Provides, otherwise Dagger won't build
    private String keywords = EMPTY_STRING;
    private String category = EMPTY_STRING;
    private Float maxprice = DEFAULT_MAX_PRICE;
    private Float minprice = DEFAULT_MIN_PRICE;
    private Map<Class, Boolean> enabledRepositoryMap = new HashMap<Class, Boolean>();
    private Context context;
    private long wishlistId;

    public ProductPickerModule(Context context, Map<Class, Boolean> enabledRepositoryMap, String category, long wishlistId) {
        this.context = context;
        this.enabledRepositoryMap = enabledRepositoryMap;
        this.category = category;
        this.wishlistId = wishlistId;
    }

    public ProductPickerModule(Context context, Map<Class, Boolean> enabledRepositoryMap, String category, String keywords, Float maxprice, Float minprice, long wishlistId) {
        this.context = context;
        this.enabledRepositoryMap = enabledRepositoryMap;
        this.category = category;
        this.keywords = keywords;
        this.maxprice = maxprice;
        this.minprice = minprice;
        this.wishlistId = wishlistId;
    }

    @Provides
    @PerActivity
    GetNetProductsUseCase provideGetNetProductListUseCase(List<ProductRepository> productRepositoryList,
                                                       CurrencyRepository currencyRepository,
                                                       EventBus eventBus) {
        return new GetNetProductsUseCase(productRepositoryList, currencyRepository, category, keywords, maxprice, minprice, wishlistId, eventBus);
    }

    //Edit this method to add new product data sources
    @Provides
    @PerActivity
    List<ProductRepository> provideProductRepositoryList(@Named("EbayRepository") ProductRepository ebayProductDataSource,
                                                                  @Named("EtsyRepository")ProductRepository etsyProductDataSource) {
        List<ProductRepository> productRepositoryList = new ArrayList<>();
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
    ProductPickerAdapter providesProductListAdapter(Picasso picasso, EventBus eventBus) {
        return new ProductPickerAdapter(context, picasso, eventBus);
    }
}
