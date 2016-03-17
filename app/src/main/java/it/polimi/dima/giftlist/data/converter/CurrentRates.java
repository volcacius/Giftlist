package it.polimi.dima.giftlist.data.converter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Elena on 15/02/2016.
 */
@Root(name = "gesmes:Envelope", strict=false)
public class CurrentRates {

    @Element(name = "Cube")
    RateListWrapper mRateList;

    public RateListWrapper getWrapper() {
        return mRateList;
    }

    @Root(name = "Cube")
    public static class RateListWrapper {
        @Element(name = "Cube")
        RateList mRateList;

        public RateList getRateList() {
            return mRateList;
        }
    }

    @Root(name = "Cube", strict=false)
    public static class RateList {
        @Attribute(name = "time", required = false)
        String time;

        @ElementList(inline = true)
        List<Rate> mRateList;

        public List<Rate> getRateList() {
            return mRateList;
        }

        public String getTime() {
            return time;
        }

        public float getConversionRate(String currency) {
            for(Rate r : mRateList) {
                if (r.getCurrency().equals(currency)) {
                    return Float.parseFloat(r.getRate());
                }
            }

            return 1;
        }
    }


    @Root(name = "Cube")
    public static class Rate {
        @Attribute(name = "currency")
        String currency;

        @Attribute(name = "rate")
        String rate;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }
}
