package it.polimi.dima.giftlist.data.net.etsy;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EtsyProduct;
import it.polimi.dima.giftlist.data.model.Product;
import timber.log.Timber;

/**
 * Created by Elena on 28/01/2016.
 */
public class EtsyResultsDeserializer implements JsonDeserializer<List<Product>> {

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CURRENCY_CODE = "currency_code";
    private static final String PRICE = "price";
    private static final String LISTING_ID = "listing_id";
    private static final String IMAGES = "Images";
    private static final String URL_570_X_N = "url_570xN";
    private static final float DEFAULT_PRICE = 0;


    @Override
    public List<Product> deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Product> etsyProductsList = new ArrayList<>();
        JsonArray resultsArray = je.getAsJsonObject().get(RESULTS).getAsJsonArray();

        for(int currentProduct = 0; currentProduct < resultsArray.size(); currentProduct++) {

            JsonElement jsonProduct = resultsArray.get(currentProduct);

            try {
                String title = jsonProduct.getAsJsonObject().get(TITLE).getAsString();
                String description = jsonProduct.getAsJsonObject().get(DESCRIPTION).getAsString();
                CurrencyType currencyType = CurrencyType.valueOf(jsonProduct.getAsJsonObject().get(CURRENCY_CODE).getAsString().toUpperCase());
                float price = jsonProduct.getAsJsonObject().get(PRICE).isJsonNull() ? DEFAULT_PRICE : jsonProduct.getAsJsonObject().get(PRICE).getAsFloat();
                long listing_id = jsonProduct.getAsJsonObject().get(LISTING_ID).getAsLong();

                JsonElement images = jsonProduct.getAsJsonObject().get(IMAGES);
                JsonArray imagesArray = images.getAsJsonArray();
                JsonElement jsonImage = imagesArray.get(0);
                String url_170x135 = jsonImage.getAsJsonObject().get(URL_570_X_N).getAsString();

                //TODO later, to get more images
            /*
            for(int currentImage = 0; currentImage < imagesArray.size(); currentImage++) {

                JsonElement jsonImage = imagesArray.get(currentImage);
                String url_170x135 = jsonImage.getAsJsonObject().get("url_170x135").getAsString();

            }*/
                EtsyProduct p = new EtsyProduct(title, description, listing_id, price, currencyType, url_170x135, "uri");
                etsyProductsList.add(p);
            } catch (Exception e) {
                Timber.d("Ebay deserializer exception message: " + e.getMessage());
            }
        }

        return  etsyProductsList;
    }
}

