package it.polimi.dima.giftlist.util;

import it.polimi.dima.giftlist.presentation.exception.NoMoreResultsFoundException;
import it.polimi.dima.giftlist.presentation.exception.NoResultsFoundException;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Elena on 13/01/2016.
 */
public class ErrorMessageDeterminer {

    public static String getErrorMessage(Throwable e) {
        if (e instanceof HttpException) {
            String message = e.getMessage();
            message = message.split("\"")[0];
            switch (message) {
                case HttpErrors.TIMEOUT:
                    return "You may have connection issues";
                case HttpErrors.UNRESOLVED_HOST:
                    return "You may have connection issues";
                case HttpErrors.SERVER_ERROR:
                    return "Sorry, there are problems on the server side";
                default:
                    return "An unknown network error has occurred :(";
            }
        } else if (e instanceof NoResultsFoundException) {
            return "No results found";
        } else if (e instanceof NoMoreResultsFoundException) {
            return "No more results found";
        } else {
            return "Unkown error";
        }
    }
}