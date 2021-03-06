package it.polimi.dima.giftlist.presentation.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.util.ImageConstants;
import timber.log.Timber;

/**
 * Created by Alessandro on 21/03/16.
 */
public class ProductPickerAdapter extends BaseAdapter {

    private static final int FIRST_POSITION = 0;

    Context context;
    LayoutInflater layoutInflater;
    Picasso picasso;
    List<Product> productList;

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
            view = layoutInflater.inflate(R.layout.view_swipable_product_card, parent, false);
            productPickerViewHolder = new ProductPickerViewHolder(view);
            view.setTag(productPickerViewHolder);
        }

        Product currentProduct = getItem(position);
        productPickerViewHolder.nameTextView.setText(currentProduct.getName());
        productPickerViewHolder.priceTextView.setAmount(currentProduct.getPrice(), currentProduct.getCurrencyType().getSymbol());
        if (currentProduct.getConvertedPrice() != 0 && !currentProduct.getCurrencyType().equals(CurrencyType.EUR)) {
            productPickerViewHolder.convertedPriceTextView.setAmount(currentProduct.getConvertedPrice(), CurrencyType.EUR.getSymbol());
        }

        if (currentProduct.getImageUrl() == null) {
            Timber.d("no image available");
            ColorDrawable colorDrawable = new ColorDrawable(Color.GRAY);
            productPickerViewHolder.productThumb.setDrawingCacheEnabled(true);
            productPickerViewHolder.productThumb.setImageDrawable(colorDrawable);
        } else {
            picasso.load(currentProduct.getImageUrl())
                    .resize(ImageConstants.IMAGE_WIDTH_VIEW, ImageConstants.IMAGE_HEIGHT_VIEW)
                    .centerCrop()
                    .into(productPickerViewHolder.productThumb);
        }

        return view;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void removeFirstProduct() {
        if (!productList.isEmpty()) {
            productList.remove(FIRST_POSITION);
        }
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
        MoneyTextView priceTextView;
        @Bind(R.id.product_price_converted)
        MoneyTextView convertedPriceTextView;

        public ProductPickerViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
