package it.polimi.dima.giftlist.presentation.view.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.OnClick;
import it.polimi.dima.giftlist.R;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EbayProduct;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import it.polimi.dima.giftlist.util.ImageConstants;
import timber.log.Timber;

/**
 * Created by Alessandro on 10/08/16.
 */
@FragmentWithArgs
public class ProductDetailsFragment extends BaseFragment {

    @Arg
    Product product;

    @Inject
    Picasso picasso;

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

    @Bind(R.id.open_website)
    Button openWebsiteButton;
    
    @BindColor(R.color.primary)
    int colorPrimary;

    @OnClick(R.id.open_website)
    public void openWebsite(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getProductPage()));
        startActivity(browserIntent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("Product details fragment view created!");
        nameTextView.setText(product.getName());
        priceTextView.setText(product.getPrice() + " " + product.getCurrencyType().toString());

        if (product.getConvertedPrice() != 0 && !product.getCurrencyType().equals(CurrencyType.EUR)) {
            convertedPriceTextView.setText("(approx. " + product.getConvertedPrice() + " " + CurrencyType.EUR.toString() + ")");
        }
        if (product instanceof EbayProduct) {
            repositoryTextView.setText(R.string.checkbox_ebay);
        } else if (product instanceof EtsyProduct) {
            repositoryTextView.setText(R.string.checkbox_etsy);
        }
        if (product.getImageUrl() == null) {
            ColorDrawable colorDrawable = new ColorDrawable(colorPrimary);
            productThumb.setDrawingCacheEnabled(true);
            productThumb.setImageDrawable(colorDrawable);
        } else {
            File f = new File(product.getImageUri());
            picasso
                    .load(f)
                    .resize(ImageConstants.IMAGE_WIDTH_VIEW, ImageConstants.IMAGE_HEIGHT_VIEW)
                    .centerCrop()
                    .into(productThumb);
        }
    }
    /*
     * I'm injecting from the app component since I need only Picasso, which doesn't require a PerActivity instance
     */
    @Override
    protected void injectDependencies() {
        getApplicationComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product_details;
    }

}
