package it.polimi.dima.giftlist.product;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.model.EtsyProduct;
import it.polimi.dima.giftlist.util.ErrorMessageDeterminer;

/**
 * Created by Elena on 19/01/2016.
 */
public class ProductFragment extends MvpLceViewStateFragment<SwipeRefreshLayout, List<EtsyProduct>, ProductView, ProductPresenter>
        implements ProductView, SwipeRefreshLayout.OnRefreshListener {

   /* @Bind(R.id.fragment_product_recyclerView)
    RecyclerView mRecyclerView;
*/
    @Bind(R.id.product_title)
    TextView mTitleTextView;
    @Bind(R.id.product_id)
    TextView mIdTextView;
    @Bind(R.id.product_image)
    TextView mImageTextView;
    @Bind(R.id.next_button)
    ImageButton mNextProduct;
    @Bind(R.id.product_thumb)
    ImageView mProductThumb;

    @BindColor(R.color.primary)           int mColorPrimary;
    @BindColor(R.color.gray)           int mColorGray;

    List<EtsyProduct> mEtsyProducts;
    int mCurrentIndex;
    String categorySelected;

    private void setParameters() {
        mTitleTextView.setText(mEtsyProducts.get(mCurrentIndex).getTitle());
        mIdTextView.setText("" + mEtsyProducts.get(mCurrentIndex).getListing_id());
        mImageTextView.setText(mEtsyProducts.get(mCurrentIndex).getImageUrl());
        if (mEtsyProducts.get(mCurrentIndex).getImageUrl() == null) {
            ColorDrawable colorDrawable = new ColorDrawable(mColorPrimary);
            mProductThumb.setDrawingCacheEnabled(true);
            mProductThumb.setImageDrawable(colorDrawable);

        } else {
            picasso.load(mEtsyProducts.get(mCurrentIndex).getImageUrl())
                    .resize(240,330)
                    .centerCrop()
                    .into(mProductThumb);
        }



      //  }
    }

    @OnClick(R.id.next_button)
    public void goNext(){
        mCurrentIndex = mCurrentIndex + 1;
        if (mCurrentIndex == 24) {
            loadData(true);
        }
        setParameters();
    }

    @Inject
    ErrorMessageDeterminer errorMessageDeterminer;

    @Inject
    Picasso picasso;


  //  ProductAdapter mProductAdapter;
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

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        categorySelected = args.getString("category");
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
        this.mEtsyProducts = data;
        mCurrentIndex = 0;
        setParameters();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
       // presenter.loadItemList(pullToRefresh);
        presenter.loadRetrofit(categorySelected, pullToRefresh);

    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
        e.printStackTrace();
    }

    @Override public void showContent() {
        super.showContent();
        mNextProduct.setEnabled(true);
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

    @Override
    public void onRefresh() {
        loadData(true);
    }
}
