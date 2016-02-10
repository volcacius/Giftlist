package it.polimi.dima.giftlist.util;

/**
 * Created by Elena on 13/01/2016.
 */
public class ErrorMessageDeterminer {

    public String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        switch (message) {
            case HttpErrors.TIMEOUT:
                return "You may have connection issues";
            case HttpErrors.SERVER_ERROR:
                return "Sorry, there are problems on the server side";
            default:
                System.out.print(message);
                return "An unknown error has occurred";
        }
    }
}