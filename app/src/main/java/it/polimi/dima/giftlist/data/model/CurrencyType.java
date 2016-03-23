package it.polimi.dima.giftlist.data.model;

/**
 * Created by Alessandro on 21/03/16.
 */
public enum CurrencyType {

     EUR("EUR"),
     USD("USD"),
     BGN("BGN"),
     JPY("JPY"),
     CZK("CZK"),
     DKK("DKK"),
     GBP("GBP"),
     HUF("HUF"),
     PLN("PLN"),
     RON("RON"),
     SEK("SEK"),
     CHF("CHF"),
     NOK("NOK"),
     HRK("HRK"),
     RUB("RUB"),
     TRY("TRY"),
     AUD("AUD"),
     BRL("BRL"),
     CAD("CAD"),
     CNY("CNY"),
     HKD("HKD"),
     IDR("IDR"),
     INR("INR"),
     KRW("KRW"),
     MXN("MXN"),
     MYR("MYR"),
     NZD("NZD"),
     PHP("PHP"),
     SGD("SGD"),
     THB("THB"),
     ZAR("ZAR"),
     ILS("ILS");

    private final String text;

    CurrencyType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
