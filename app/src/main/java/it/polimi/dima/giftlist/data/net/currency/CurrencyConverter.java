package it.polimi.dima.giftlist.data.net.currency;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.CurrencyType;

/**
 * Created by Alessandro on 22/03/16.
 */
public class CurrencyConverter implements Converter<Currency> {

    private static final String CURRENCY = "currency";
    private static final String RATE = "rate";

    @Override
    public Currency read(InputNode node) throws Exception {
        CurrencyType currencyType = CurrencyType.valueOf(node.getAttribute(CURRENCY).getValue().toString().toUpperCase());
        float rate = Float.valueOf(node.getAttribute(RATE).getValue());
        return new Currency(currencyType, rate);
    }

    @Override
    public void write(OutputNode node, Currency value) throws Exception {
        node.setAttribute(CURRENCY, value.getCurrencyType().toString());
        node.setAttribute(RATE, String.valueOf(value.getRate()));
    }
}
