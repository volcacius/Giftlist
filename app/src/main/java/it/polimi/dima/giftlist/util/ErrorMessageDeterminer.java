package it.polimi.dima.giftlist.util;

/**
 * Created by Elena on 13/01/2016.
 */
public class ErrorMessageDeterminer {

    public String getErrorMessage(Throwable e) {
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
                return "An unknown error has occurred :(";
        }
    }
}