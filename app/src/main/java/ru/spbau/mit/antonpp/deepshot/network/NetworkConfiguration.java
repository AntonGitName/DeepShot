package ru.spbau.mit.antonpp.deepshot.network;

/**
 * @author antonpp
 * @since 20/11/15
 */
public class NetworkConfiguration {
    private final static String DEFAULT_IP = "192.168.1.19";

    public static String SERVER_IP;
    public static String SERVER_ADDRESS;

    public static String URL_GET_LIST_STYLES;
    public static String URL_GET_FILTER;

    public static String URL_GET_LIST_RESULTS;
    public static String URL_GET_RESULT;

    public static String URL_POST_IMAGE;

    static {
        resetIp(DEFAULT_IP);
    }

    public static void resetIp(String ip) {
        SERVER_IP = ip;
        SERVER_ADDRESS = "http://" + SERVER_IP + ":8080";

        URL_GET_LIST_STYLES = SERVER_ADDRESS + "/styles/list";
        URL_GET_FILTER = SERVER_ADDRESS + "/styles";

        URL_GET_LIST_RESULTS = SERVER_ADDRESS + "/results/list";
        URL_GET_RESULT = SERVER_ADDRESS + "/results";

        URL_POST_IMAGE = SERVER_ADDRESS + "/send";
    }

}
