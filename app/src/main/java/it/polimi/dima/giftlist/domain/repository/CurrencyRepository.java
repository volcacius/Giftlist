package it.polimi.dima.giftlist.domain.repository;

import java.util.List;

import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.net.currency.CurrencyXmlModel;
import rx.Observable;

/**
 * Created by Alessandro on 21/03/16.
 */
public interface CurrencyRepository {
    Observable<List<CurrencyXmlModel.Currency>> getCurrencyList();
    Observable<CurrencyXmlModel.Currency> getCurrency(CurrencyType currencyType);
}
