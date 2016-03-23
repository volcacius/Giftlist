package it.polimi.dima.giftlist.data.repository.datasource;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import it.polimi.dima.giftlist.data.model.Currency;
import it.polimi.dima.giftlist.data.model.CurrencyType;
import it.polimi.dima.giftlist.data.net.currency.CurrencyApi;
import it.polimi.dima.giftlist.data.net.currency.CurrencyXmlModel;
import it.polimi.dima.giftlist.domain.repository.CurrencyRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by Elena on 14/02/2016.
 */
public class CurrencyDataSource implements CurrencyRepository {

    CurrencyApi currencyApi;
    Observable<List<Currency>> currencyList;
    EventBus eventBus;

    @Inject
    public CurrencyDataSource(CurrencyApi currencyApi, EventBus eventBus) {
        this.currencyApi = currencyApi;
        this.eventBus = eventBus;
    }

    //.cache() avoids a call to the API each time someone subscribe to the observable and instead caches the results of the API observable
    private void fetchRates() {
        currencyList = currencyApi
                        .getCurrencies()
                        .cache()
                        .flatMap(currencyXmlModel -> Observable.from(currencyXmlModel.getCurrencyXmlListWrapper().getCurrencyXmlList().getCurrencyList()))
                        .toList();

    }

    @Override
    public Observable<List<Currency>> getCurrencyList() {
        if (currencyList == null) {
            fetchRates();
        }
        return currencyList;
    }
}
