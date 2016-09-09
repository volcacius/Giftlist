package it.polimi.dima.giftlist.presentation.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import hugo.weaving.DebugLog;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.presentation.event.AdapterAboutToEmptyEvent;
import it.polimi.dima.giftlist.presentation.event.ProductAddedEvent;
import it.polimi.dima.giftlist.presentation.event.ProductImageSavedEvent;
import it.polimi.dima.giftlist.presentation.exception.UnknownProductException;
import it.polimi.dima.giftlist.presentation.view.ProductPickerView;
import it.polimi.dima.giftlist.domain.interactor.GetNetProductsUseCase;
import it.polimi.dima.giftlist.util.ImageConstants;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductPickerPresenter extends BaseRxLcePresenter<ProductPickerView, List<Product>, GetNetProductsUseCase> {

    private static final boolean NO_PULL_TO_REFRESH = false;

    private boolean isSubscriptionPending;
    Picasso picasso;
    EventBus eventBus;

    //needed to avoid that the GC collects the image before it is stored
    final List<Target> targets;

    @Inject
    public ProductPickerPresenter(EventBus eventBus, GetNetProductsUseCase getNetProductsUseCase, StorIOSQLite db, Picasso picasso) {
        super(getNetProductsUseCase, db);
        this.eventBus = eventBus;
        this.picasso = picasso;
        this.isSubscriptionPending = false;
        this.targets = new ArrayList<Target>();
    }

    /**
     * Subscribes the presenter himself as subscriber on the observable
     * This pattern of calling onCompleted, onError and onNext of the Presenter from the inner subscriber class
     * allows, if necessary, to override them from classes that extends the Presenter while leaving the subscriber untouched.
     * To get an idea, see e.g. https://ideone.com/mIeavZ
     *
     * ShowLoading is not called here, otherwise it would show the loading view when subscribing in background, even if there
     * is still stuff in the adapter. ShowLoading is called only as an empty view of the adapter
     *
     * @param pullToRefresh Pull to refresh?
     */
    @Override
    @DebugLog
    public void subscribe(boolean pullToRefresh) {
        useCase.execute(new BaseSubscriber(pullToRefresh));
    }

    @Override
    protected void onCompleted() {
        if (isViewAttached()) {
            getView().showContent();
        }
        unsubscribe();
        runPendingSubscription();
    }

    @Override
    protected void onError(Throwable e, boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showError(e, pullToRefresh);
        }
        unsubscribe();
        runPendingSubscription();
    }

    @Override
    protected void onNext(List<Product> data) {
        getView().appendData(data);
        getView().showContent();
    }

    @Override
    public void attachView(ProductPickerView view) {
        super.attachView(view);
        if (eventBus!=null) {
            eventBus.register(this);
        }
    }

    /*
     * This gets automatically called by Mosby
     */
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            unsubscribe();
        }
        if (eventBus!=null) {
            eventBus.unregister(this);
        }
    }

    //This is way to manage messages back from the view layer. When the adapter is emptying,
    //it sends a message to the presenter to remind it to load more data. If a subscription isn't ongoing,
    //subscribe. Otherwise, if a subscription is already open and there is no pending future subscription,
    //set a subscription as pending.
    @DebugLog
    @Subscribe
    public void onAdapterEmptyEvent(AdapterAboutToEmptyEvent event) {
        if (useCase.isUnsubscribed()) {
            subscribe(NO_PULL_TO_REFRESH);
        } else if (!isSubscriptionPending) {
            isSubscriptionPending = true;
        }
    }



    @Subscribe
    public void onProductAddedEvent(ProductAddedEvent event) throws UnknownProductException {
        Product product = event.getProduct();
        saveProductImage(product);
    }

    @Subscribe
    public void onProductImageSaved(ProductImageSavedEvent event) throws UnknownProductException {
        Product product = event.getProduct();
        Timber.d("converted price :" + product.getConvertedPrice());
        product.setDisplayOrder(getView().getProductDisplayOrder());
        getProductPutObservable(product).subscribe(new SingleSubscriber<PutResult>() {
            @Override
            public void onSuccess(PutResult value) {
                getView().showProductAddedSuccess();
            }
            @Override
            public void onError(Throwable error) {
                getView().showProductAddedError();
            }
        });
        getView().setNextProductDisplayOrder();
        Timber.d("New order is %d", getView().getProductDisplayOrder());
    }

    @RxLogObservable
    private Single<PutResult> getProductPutObservable(Product product) throws UnknownProductException {
        //all Observables in StorIO already subscribed on Schedulers.io(), you just need to set observeOn()
        if (product instanceof EbayProduct) {
            return db.put()
                    .object((EbayProduct) product)
                    .prepare()
                    .asRxSingle()
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (product instanceof EtsyProduct) {
            return db.put()
                    .object((EtsyProduct) product)
                    .prepare()
                    .asRxSingle()
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            throw new UnknownProductException();
        }
    }

    //Check if there is a pending subscription to register
    private void runPendingSubscription() {
        if (isSubscriptionPending) {
            subscribe(NO_PULL_TO_REFRESH);
            isSubscriptionPending = false;
        }
    }

    public void saveProductImage(Product product) throws UnknownProductException {

        Target myTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    Timber.d("picasso is saving pic: " + product.getName());
                    File myDir = new File(product.getImageUri());
                    FileOutputStream out = new FileOutputStream(myDir);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    eventBus.post(new ProductImageSavedEvent(product));
                } catch(Exception e){
                    Timber.d("picasso error " + e.getMessage());
                }
                targets.remove(this);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Timber.d("picasso onBitmapFailed");
                //Better to remove uri?
                eventBus.post(new ProductImageSavedEvent(product));
                targets.remove(this);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Timber.d("picasso onPrepareLoad");
            }
        };

        targets.add(myTarget);
        picasso
                .load(product.getImageUrl())
                .resize(ImageConstants.IMAGE_WIDTH_SAVE, ImageConstants.IMAGE_HEIGHT_SAVE)//The other dimension will be scaled properly preserving aspect ratio
                .centerInside()
                .onlyScaleDown()
                .into(myTarget);
    }
}


