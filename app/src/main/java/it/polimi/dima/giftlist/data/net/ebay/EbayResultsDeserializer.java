package it.polimi.dima.giftlist.data.net.ebay;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.model.EbayProduct;

/**
 * Created by Elena on 26/03/2016.
 */
public class EbayResultsDeserializer implements JsonDeserializer {

    @Override
    public Object deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<EbayProduct> ebayProductsList = new ArrayList<>();

        JsonElement results = je.getAsJsonObject().get("findItemsByKeywordsResponse")
                .getAsJsonArray().get(0)                //if you don't catch them as json arrays first, it complains
                .getAsJsonObject().get("searchResult")
                .getAsJsonArray().get(0)                //with a NOT a Json object exception
                .getAsJsonObject().get("item");


        JsonArray resultsArray = results.getAsJsonArray();

        for(int currentProduct = 0; currentProduct < resultsArray.size(); currentProduct++) {
            JsonElement jsonProduct = resultsArray.get(currentProduct);
            try{
                //TODO Add try - catch Attempt to invoke virtual method 'java.lang.String com.google.gson.JsonElement.getAsString()' on a null object reference
                String title = jsonProduct.getAsJsonObject().get("title").getAsString();
                String listing_id = jsonProduct.getAsJsonObject().get("itemId").getAsString();

                //String description = jsonProduct.getAsJsonObject().get("description").getAsString();

                JsonElement priceInfo = jsonProduct.getAsJsonObject().get("sellingStatus")
                        .getAsJsonArray().get(0).getAsJsonObject()
                        .get("currentPrice").getAsJsonArray().get(0);

                float price;
                CurrencyType currencyType;

                String pricePattern = "\"([0-9]*\\.[0-9]*)\"";
                String currencyPattern = "\"([A-Z]*)\"";

                Pattern pat = Pattern.compile(pricePattern);
                Matcher m = pat.matcher(priceInfo.toString());


                if (m.find()) {
                    price = Float.parseFloat(m.group(1));
                }
                else {
                    price = 0;
                }

                pat = Pattern.compile(currencyPattern);
                m = pat.matcher(priceInfo.toString());

                if (m.find()) {
                    currencyType = CurrencyType.valueOf(m.group(1).toUpperCase());
                }
                else {
                    currencyType = CurrencyType.EUR;
                }

                String url_170x135 = jsonProduct.getAsJsonObject().get("galleryURL").getAsString();
                String url_page = jsonProduct.getAsJsonObject().get("viewItemURL").getAsString();

                EbayProduct p = new EbayProduct(title, "something", listing_id, price, currencyType, url_170x135, url_page);
                ebayProductsList.add(p);
            } catch (NullPointerException e) {
                System.out.println("problem: null json object " + e.getMessage());
            }
        }
        return  ebayProductsList;
    }

}
