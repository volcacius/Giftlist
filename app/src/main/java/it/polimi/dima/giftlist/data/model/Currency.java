package it.polimi.dima.giftlist.data.model;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import it.polimi.dima.giftlist.data.net.currency.CurrencyConverter;

/**
 * Created by Alessandro on 22/03/16.
 */
@Root(name = "Cube")
@Convert(CurrencyConverter.class)
public class Currency {

    CurrencyType currencyType;
    float rate;

    public Currency(CurrencyType currencyType, float rate) {
        this.currencyType = currencyType;
        this.rate = rate;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

}
