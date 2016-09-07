package it.polimi.dima.giftlist.presentation.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.db.table.EbayProductTable;
import it.polimi.dima.giftlist.data.db.table.EtsyProductTable;
import it.polimi.dima.giftlist.data.db.table.WishlistTable;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.data.model.Wishlist;
import it.polimi.dima.giftlist.domain.interactor.GetDbProductListUseCase;
import it.polimi.dima.giftlist.presentation.event.ProductRemovedEvent;
import it.polimi.dima.giftlist.presentation.event.WishlistAddedEvent;
import it.polimi.dima.giftlist.presentation.view.WishlistView;
import it.polimi.dima.giftlist.presentation.view.activity.BaseActivity;
import rx.Observer;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Alessandro on 18/03/16.
 */
public class WishlistPresenter extends BaseRxLcePresenter<WishlistView, List<Product>, GetDbProductListUseCase> {

    @Inject
    public WishlistPresenter(GetDbProductListUseCase getDbProductListUseCase, StorIOSQLite db) {
        super(getDbProductListUseCase, db);
    }

    @Override
    public void subscribe(boolean pullToRefresh) {
        if (!useCase.isUnsubscribed()) {
            unsubscribe();
        }
        useCase.execute(new BaseSubscriber(pullToRefresh));
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }
    }

    @Override
    protected void onCompleted() {
        //DB subscriptions do not complete
   }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    @DebugLog
    protected void onNext(List<Product> data) {
        getView().showLoading(false);
        List<Product> orderedList = new LinkedList<>(data);
        Collections.sort(orderedList);
        getView().setData(orderedList);
        if (isViewAttached()) {
            getView().showContent();
        }
    }

    public void setActionBarDetails(long wishlistId) {
        db.get()
                .object(Wishlist.class)
                .withQuery(Query.builder()
                        .table(WishlistTable.TABLE)
                        .where("id = ?")
                        .whereArgs(wishlistId)
                        .build())
                .prepare()
                .asRxSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Wishlist>() {
                    @Override
                    public void onSuccess(Wishlist value) {
                        getView().initCollapsingToolbar(value.getName(), value.getOccasion());
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.d("Can't load wishlist details");
                    }
                });
    }

    //I do not want the observer to emit an unnecessary onNext
    //So I manually run an update query without adding the affected table
    public void updateProductListOrder(List<Product> productList) {
        for (Product p : productList) {
            if (p instanceof EbayProduct) {
                db.executeSQL()
                        .withQuery(RawQuery.builder()
                                .query(EbayProductTable.getDisplayOrderUpdateQuery(p.getId(), p.getDisplayOrder()))
                                .build())
                        .prepare()
                        .asRxSingle()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<Object>() {
                            @Override
                            public void onSuccess(Object value) {
                                Timber.d("Product %d is set at order %d in DB", p.getId(), p.getDisplayOrder());
                            }

                            @Override
                            public void onError(Throwable error) {
                                Timber.d("Error in setting product %d at order %d in DB", p.getId(), p.getDisplayOrder());
                            }
                        });
            }
            if (p instanceof EtsyProduct) {
                db.executeSQL()
                        .withQuery(RawQuery.builder()
                                .query(EtsyProductTable.getDisplayOrderUpdateQuery(p.getId(), p.getDisplayOrder()))
                                .build())
                        .prepare()
                        .asRxSingle()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<Object>() {
                            @Override
                            public void onSuccess(Object value) {
                                Timber.d("Product %d is set at order %d in DB", p.getId(), p.getDisplayOrder());
                            }

                            @Override
                            public void onError(Throwable error) {
                                Timber.d("Error in setting product %d at order %d in DB", p.getId(), p.getDisplayOrder());
                            }
                        });
            }
        }
    }

    public void removeProduct(Product product) {
        deleteImages(product.getImageUri());
        long productId = product.getId();
        if (product instanceof EtsyProduct) {
            db.executeSQL()
                    .withQuery(RawQuery.builder()
                            .query(EtsyProductTable.getCustomDeleteQuery(productId))
                            .build())
                    .prepare()
                    .asRxSingle()
                    .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                    .subscribe(new SingleSubscriber<Object>() {
                        @Override
                        public void onSuccess(Object value) {
                            Timber.d("Success in deleting the product %d", productId);
                        }

                        @Override
                        public void onError(Throwable error) {
                            Timber.d("Error in deleting the product %d", productId);
                        }
                    });
        }

        if (product instanceof EbayProduct) {
            db.executeSQL()
                    .withQuery(RawQuery.builder()
                            .query(EbayProductTable.getCustomDeleteQuery(productId))
                            .build())
                    .prepare()
                    .asRxSingle()
                    .observeOn(AndroidSchedulers.mainThread()) //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
                    .subscribe(new SingleSubscriber<Object>() {
                        @Override
                        public void onSuccess(Object value) {
                            Timber.d("Success in deleting the product %d", productId);
                        }

                        @Override
                        public void onError(Throwable error) {
                            Timber.d("Error in deleting the product %d", productId);
                        }
                    });
        }
    }

    private void deleteImages(String uri) {
        File fdelete = new File(uri);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Timber.d("file Deleted: %s", uri);
            } else {
                Timber.d("file not Deleted %s", uri);
            }
        }

    }

}
