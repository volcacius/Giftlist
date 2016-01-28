package it.polimi.dima.giftlist.product.Rest;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Elena on 28/01/2016.
 */
public class EtsyResultsDeserializer<T> implements JsonDeserializer<List<T>> {
    @Override
    public List<T> deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement results = je.getAsJsonObject().get("results");

        return new Gson().fromJson(results, typeOfT);
    }
}
