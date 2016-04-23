package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;

/**
 * Created by Alessandro on 21/03/16.
 */
public class ProductListAdapter extends BaseAdapter {

    private static final int IMAGE_WIDTH = 240;
    private static final int IMAGE_HEIGHT = 330;
    private static final int FIRST_POSITION = 0;

    Context context;
    LayoutInflater layoutInflater;
    Picasso picasso;
    List<Product> productList;

    @Inject
    public ProductListAdapter(Context context, Picasso picasso) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.picasso = picasso;
        this.productList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ProductListViewHolder productListViewHolder;
        if (view != null) {
            productListViewHolder = (ProductListViewHolder) view.getTag();
        } else {
            view = layoutInflater.inflate(R.layout.view_product, parent, false);
            productListViewHolder = new ProductListViewHolder(view);
            view.setTag(productListViewHolder);
        }
        Product currentProduct = getItem(position);
        productListViewHolder.nameTextView.setText(currentProduct.getName());
        productListViewHolder.priceTextView.setText(currentProduct.getPrice() + " " + currentProduct.getCurrencyType().toString());
        if(currentProduct.getConvertedPrice() != 0 && !currentProduct.getCurrencyType().equals(CurrencyType.EUR)) {
            productListViewHolder.convertedPriceTextView.setText("(" + currentProduct.getConvertedPrice() + " " + CurrencyType.EUR.toString() + ")");
        }
        if(currentProduct instanceof EbayProduct) {
            productListViewHolder.repositoryTextView.setText(R.string.checkbox_ebay);
        }
        if(currentProduct instanceof EtsyProduct) {
            productListViewHolder.repositoryTextView.setText(R.string.checkbox_etsy);
        }
        if (currentProduct.getImageUrl() == null) {
            ColorDrawable colorDrawable = new ColorDrawable(productListViewHolder.colorPrimary);
            productListViewHolder.productThumb.setDrawingCacheEnabled(true);
            productListViewHolder.productThumb.setImageDrawable(colorDrawable);

        } else {
            picasso.load(currentProduct.getImageUrl())
                    .resize(IMAGE_WIDTH,IMAGE_HEIGHT)
                    .centerCrop()
                    .into(productListViewHolder.productThumb);
        }
        return view;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void removeFirstProduct() {
        productList.remove(FIRST_POSITION);
        this.notifyDataSetChanged();
    }

    public void appendProductList(List<Product> data) {
        this.productList.addAll(data);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    static class ProductListViewHolder {

        @Bind(R.id.product_name)
        TextView nameTextView;
        @Bind(R.id.product_thumb)
        ImageView productThumb;
        @Bind(R.id.product_price)
        TextView priceTextView;
        @Bind(R.id.product_price_converted)
        TextView convertedPriceTextView;
        @Bind(R.id.text_repository)
        TextView repositoryTextView;
        @BindColor(R.color.primary)
        int colorPrimary;

        public ProductListViewHolder(View productListView) {
            ButterKnife.bind(this, productListView);
        }
    }



}
