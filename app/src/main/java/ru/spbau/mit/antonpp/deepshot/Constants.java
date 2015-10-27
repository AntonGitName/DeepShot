package ru.spbau.mit.antonpp.deepshot;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class Constants {

    public static final String PREFERENCES = "ru.spbau.mit.antonpp.deepshot";
    public static final String SERVER_IP = "192.168.1.19";
    public static final String SERVER_ADDRESS = "http://" + SERVER_IP + ":8080";

    public static final String URL_GET_LIST_FILTERS = SERVER_ADDRESS + "/filter/all";
    public static final String URL_GET_FILTER = SERVER_ADDRESS + "/filter";

    private Constants() {
        throw new UnsupportedOperationException();
    }
}
