package it.polimi.dima.giftlist.product;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.product.converter.RetrofitEvent;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

/**
 * Created by Elena on 19/01/2016.
 */
public class ProductFragment extends MvpLceViewStateFragment<SwipeRefreshLayout, List<EtsyProduct>, ProductView, ProductPresenter>
        implements ProductView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.product_title)
    TextView mTitleTextView;
    @Bind(R.id.product_id)
    TextView mIdTextView;
    @Bind(R.id.next_button)
    ImageButton mNextProduct;
    @Bind(R.id.product_thumb)
    ImageView mProductThumb;
    @Bind(R.id.product_price)
    TextView mPriceTextView;
    @Bind(R.id.product_price_converted)
    TextView mConvertedPriceTextView;
    @Bind(R.id.reload_button)
    Button mReloadButton;

    @BindColor(R.color.primary)           int mColorPrimary;
    @BindColor(R.color.gray)           int mColorGray;

    List<EtsyProduct> mEtsyProducts;
    int mCurrentIndex;
    EtsyProduct mCurrentProduct;

    String categorySelected;
    String keywords;

    private void setParameters() {
        mTitleTextView.setText(mCurrentProduct.getTitle());
        mIdTextView.setText("" + mCurrentProduct.getListing_id());
        mPriceTextView.setText("" + mCurrentProduct.getPrice()+" "+mCurrentProduct.getCurrency());
        if(mCurrentProduct.getConvertedPrice()!=0 && mCurrentProduct.getCurrency()!="EUR") {
            mConvertedPriceTextView.setText("(" + mCurrentProduct.getConvertedPrice() + " EUR)");
        }
        if (mCurrentProduct.getImageUrl() == null) {
            ColorDrawable colorDrawable = new ColorDrawable(mColorPrimary);
            mProductThumb.setDrawingCacheEnabled(true);
            mProductThumb.setImageDrawable(colorDrawable);

        } else {
            picasso.load(mCurrentProduct.getImageUrl())
                    .resize(240,330)
                    .centerCrop()
                    .into(mProductThumb);
        }



      //  }
    }

    @OnClick(R.id.next_button)
    public void goNext(){
        mCurrentIndex = mCurrentIndex + 1;
        mCurrentProduct = new EtsyProduct(mEtsyProducts.get(mCurrentIndex));
        if (mCurrentIndex == mEtsyProducts.size()-1) {
            loadData(true);
        }
        setParameters();
    }

    @OnClick(R.id.reload_button)
    public void reloadItems(){
        loadData(true);
    }

    @Inject
    ErrorMessageDeterminer errorMessageDeterminer;

    @Inject
    Picasso picasso;

    ProductComponent mProductComponent;

    protected void injectDependencies() {
        mProductComponent =
                DaggerProductComponent.builder().productModule(new ProductModule(getActivity())).build();
        mProductComponent.inject(this);
    }

    @NonNull
    @Override
    public ProductPresenter createPresenter() {
        return mProductComponent.providePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        //previously: loaddata
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        categorySelected = args.getString("category");
        keywords = args.getString("keywords");

        injectDependencies();
        //ButterKnife is done in onViewCreate, and also setLayoutManager
        return inflater.inflate(R.layout.fragment_product, container, false); //xml file name
    }

    @NonNull
    @Override
    public LceViewState<List<EtsyProduct>, ProductView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<EtsyProduct> getData() {
        return null;// mProductAdapter.getEtsyProductList();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorMessageDeterminer.getErrorMessage(e, pullToRefresh);
    }

    @Override
    public void setData(List<EtsyProduct> data) {
        System.out.println("called setData");
        if (data != null) {
            this.mEtsyProducts = new ArrayList<>();
            for (EtsyProduct d : data) {
                mEtsyProducts.add(new EtsyProduct(d));
            }
            mCurrentIndex = 0;

        } else System.out.println("null data");

        if(mEtsyProducts.size()>0) {
            if (mCurrentProduct == null) {
                mCurrentProduct = mEtsyProducts.get(mCurrentIndex);
            }
            setParameters();
        } else {
            Toast.makeText(getContext(), "no results found, new general request issued", Toast.LENGTH_LONG).show();
            keywords="";
            presenter.loadRetrofit(categorySelected,true);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadRetrofit(categorySelected, keywords, pullToRefresh);

    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
        mReloadButton.setVisibility(View.VISIBLE);
        mReloadButton.setEnabled(true);
        e.printStackTrace();
    }

    @Override public void showContent() {
        super.showContent();
        mNextProduct.setEnabled(true);
        mReloadButton.setVisibility(View.GONE);
        mReloadButton.setEnabled(false);
        contentView.setRefreshing(false);
    }

    @Override public void showLoading(boolean pullToRefresh) {  //callde by BaseRxLcePresenter in method subscribe()
        mNextProduct.setEnabled(false);
        super.showLoading(pullToRefresh);
        if (pullToRefresh && !contentView.isRefreshing()) {
            // Workaround for measure bug: https://code.google.com/p/android/issues/detail?id=77712
            contentView.post(new Runnable() {
                @Override public void run() {
                    contentView.setRefreshing(true);
                }
            });
        }
    }

    @Override public void onViewStateInstanceRestored(boolean instanceStateRetained) {
        System.out.println("restored");
    }

    @Override public void onNewViewStateInstance() {
        //loadData(true);
        System.out.println("created");
    }

    @Override
    public void onRefresh() {
        //TODO check why it is triggered also on click :S
        //loadData(true);
    }
}
