package it.polimi.dima.giftlist.data.net.currency;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import it.polimi.dima.giftlist.data.model.CurrencyType;

/**
 * Created by Elena on 15/02/2016.
 */
@Root(name = "gesmes:Envelope", strict=false)
public class CurrencyXmlModel {

    @Element(name = "Cube")
    CurrencyXmlListWrapper currencyXmlListWrapper;

    public CurrencyXmlListWrapper getCurrencyXmlListWrapper() {
        return currencyXmlListWrapper;
    }

    @Root(name = "Cube")
    public static class CurrencyXmlListWrapper {

        @Element(name = "Cube")
        CurrencyXmlList currencyXmlList;

        public CurrencyXmlList getCurrencyXmlList() {
            return currencyXmlList;
        }
    }

    @Root(name = "Cube", strict=false)
    public static class CurrencyXmlList {

        @Attribute(name = "time", required = false)
        Long time;

        @ElementList(inline = true)
        List<Currency> currencyList;

        public List<Currency> getCurrencyList() {
            return currencyList;
        }

        public Long getTime() {
            return time;
        }
    }


    @Root(name = "Cube")
    public static class Currency {

        @Attribute(name = "currency")
        CurrencyType currencyType;

        @Attribute(name = "rate")
        float rate;

        Long time;

        public CurrencyType getCurrencyType() {
            return currencyType;
        }

        public float getRate() {
            return rate;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }
}
