package ru.spbau.mit.antonpp.deepshot;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class Constants {

    public static final String PREFERENCES = "ru.spbau.mit.antonpp.deepshot";
    public static final String SERVER_IP = "192.168.1.19";
    public static final String SERVER_ADDRESS = "http://" + SERVER_IP + ":8080";

    public static final String URL_GET_LIST_STYLES = SERVER_ADDRESS + "/styles/list";
    public static final String URL_GET_FILTER = SERVER_ADDRESS + "/styles";

    public static final String URL_GET_LIST_RESULTS = SERVER_ADDRESS + "/results/list";
    public static final String URL_GET_RESULT = SERVER_ADDRESS + "/results";

    public static final String URL_POST_IMAGE = SERVER_ADDRESS + "/send";

    public static final String CACHE_FILE = "cache.txt";

    private Constants() {
        throw new UnsupportedOperationException();
    }
}
