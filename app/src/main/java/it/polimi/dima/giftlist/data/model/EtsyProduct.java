package it.polimi.dima.giftlist.data.model;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Alessandro on 08/01/16.
 */

//TODO: conviene non far persistere nel db la valuta convertita, ma solo quella originale e la currency type. Poi quando l'oggetto viene recuperato dal db,
// la query fa la join con la tabella delle currencies (currencytype, rate), calcola il valore convertito e lo setta nell'oggetto
public class EtsyProduct extends Product {

    int categoryId;

    public EtsyProduct(String name, String description, long id, float price, CurrencyType currencyType, String imageUrl) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.price = price;
        this.currencyType = currencyType;
        this.imageUrl = imageUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
