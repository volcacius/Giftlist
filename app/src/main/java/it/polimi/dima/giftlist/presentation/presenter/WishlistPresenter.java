package it.polimi.dima.giftlist.presentation.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

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
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Alessandro on 18/03/16.
 */
public class WishlistPresenter extends BaseRxLcePresenter<WishlistView, List<Product>, GetDbProductListUseCase> {

    @Inject
    public WishlistPresenter(GetDbProductListUseCase getDbProductListUseCase, StorIOSQLite db) {
        super(null, getDbProductListUseCase, db);
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
        unsubscribe();
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
    }

    @Override
    protected void onNext(List<Product> data) {
        getView().setData(data);
        if (isViewAttached()) {
            getView().showContent();
        }
    }

    public void removeProduct(Product product) {
        long id = product.getId();
        if (product instanceof EbayProduct) {
            db.delete()
                    .byQuery(DeleteQuery.builder()
                            .table(EbayProductTable.TABLE)
                            .where(EbayProductTable.COLUMN_ID + "= ?")
                            .whereArgs(id)
                            .build())
                    .prepare()
                    .executeAsBlocking();
        } else {
            db.delete()
                    .byQuery(DeleteQuery.builder()
                            .table(EtsyProductTable.TABLE)
                            .where(EtsyProductTable.COLUMN_ID + "= ?")
                            .whereArgs(id)
                            .build())
                    .prepare()
                    .executeAsBlocking();
        }

        deleteImages(product.getImageUri());

    }

    private void deleteImages(String uri) {

        File fdelete = new File(uri);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Timber.d("file Deleted");
            } else {
                Timber.d("file not Deleted");
            }
        }

    }

}
