package it.polimi.dima.giftlist.presentation.module;

import android.content.Context;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.polimi.dima.giftlist.data.DummyInterface;
import it.polimi.dima.giftlist.data.DummyList;
import it.polimi.dima.giftlist.data.repository.ProductRepository;
import it.polimi.dima.giftlist.data.repository.datasource.EtsyProductDataSource;
import it.polimi.dima.giftlist.domain.executor.PostExecutionThread;
import it.polimi.dima.giftlist.domain.executor.ThreadExecutor;
import it.polimi.dima.giftlist.domain.interactor.GetProductListUseCase;
import it.polimi.dima.giftlist.domain.interactor.UseCase;
import it.polimi.dima.giftlist.scope.PerActivity;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

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
    GetProductListUseCase provideGetUserListUseCase(ProductRepository productRepository,
                                      ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread) {
        return new GetProductListUseCase(category, keywords, productRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    public ErrorMessageDeterminer providesErrorMessageDeterminer(){
        return new ErrorMessageDeterminer();
    }

    @Provides
    @PerActivity
    EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @PerActivity
    Picasso providesPicasso() {
        return Picasso.with(context);
    }
}
