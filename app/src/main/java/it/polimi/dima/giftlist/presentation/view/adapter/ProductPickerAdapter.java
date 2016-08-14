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
public class ProductPickerAdapter extends BaseAdapter {

    private static final int IMAGE_WIDTH = 240;
    private static final int IMAGE_HEIGHT = 330;
    private static final int FIRST_POSITION = 0;

    Context context;
    LayoutInflater layoutInflater;
    Picasso picasso;
    List<Product> productList;

    @Inject
    public ProductPickerAdapter(Context context, Picasso picasso) {
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
        ProductPickerViewHolder productPickerViewHolder;

        if (view != null) {
            productPickerViewHolder = (ProductPickerViewHolder) view.getTag();
        } else {
            view = layoutInflater.inflate(R.layout.view_product, parent, false);
            productPickerViewHolder = new ProductPickerViewHolder(view);
            view.setTag(productPickerViewHolder);
        }

        Product currentProduct = getItem(position);
        productPickerViewHolder.nameTextView.setText(currentProduct.getName());
        productPickerViewHolder.priceTextView.setText(currentProduct.getPrice() + " " + currentProduct.getCurrencyType().toString());
        if (currentProduct.getConvertedPrice() != 0 && !currentProduct.getCurrencyType().equals(CurrencyType.EUR)) {
            productPickerViewHolder.convertedPriceTextView.setText("(" + currentProduct.getConvertedPrice() + " " + CurrencyType.EUR.toString() + ")");
        }
        if (currentProduct instanceof EbayProduct) {
            productPickerViewHolder.repositoryTextView.setText(R.string.checkbox_ebay);
        } else if (currentProduct instanceof EtsyProduct) {
            productPickerViewHolder.repositoryTextView.setText(R.string.checkbox_etsy);
        }
        if (currentProduct.getImageUrl() == null) {
            ColorDrawable colorDrawable = new ColorDrawable(productPickerViewHolder.colorPrimary);
            productPickerViewHolder.productThumb.setDrawingCacheEnabled(true);
            productPickerViewHolder.productThumb.setImageDrawable(colorDrawable);
        } else {
            picasso.load(currentProduct.getImageUrl())
                    .resize(IMAGE_WIDTH,IMAGE_HEIGHT)
                    .centerCrop()
                    .into(productPickerViewHolder.productThumb);
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

    static class ProductPickerViewHolder {

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

        public ProductPickerViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



}
