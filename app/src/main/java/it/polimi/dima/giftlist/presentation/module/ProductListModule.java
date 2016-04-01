package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.model.CurrencyType;
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
    private Context context;

    public ProductListModule(Context context, String category) {
        this.context = context;
        this.category = category;
    }

    public ProductListModule(Context context, String category, String keywords) {
        this.context = context;
        this.category = category;
        this.keywords = keywords;
    }

    @Provides
    @PerActivity
    GetProductListUseCase provideGetProductListUseCase(EbayProductDataSource etsyRepository,  CurrencyRepository currencyRepository, EventBus eventBus) {
        return new GetProductListUseCase(etsyRepository, currencyRepository, category, keywords, eventBus);
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
