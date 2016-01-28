package it.polimi.dima.giftlist.product;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.model.EtsyProduct;

/**
 * Created by Elena on 27/01/2016.
 */
public class ProductAdapter  extends SupportAnnotatedAdapter implements ProductAdapterBinder {


    @ViewType(
            layout = R.layout.list_product,
            views = {
                    @ViewField(id = R.id.item_name, type = TextView.class, name = "name")
            }) public final int wishlist = 0;

    List<EtsyProduct> mEtsyProductList;

    @Inject
    public ProductAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return mEtsyProductList == null ? 0 : mEtsyProductList.size();
    }

    public List<EtsyProduct> getEtsyProductList() {
        return mEtsyProductList;
    }

    public void setEtsyProductList(List<EtsyProduct> etsyProductList) {
        this.mEtsyProductList = etsyProductList;
    }

    @Override
    public void bindViewHolder(ProductAdapterHolders.WishlistViewHolder vh, int position) {
        EtsyProduct etsyProduct = mEtsyProductList.get(position);
        vh.name.setText(etsyProduct.getTitle());
    }
}
