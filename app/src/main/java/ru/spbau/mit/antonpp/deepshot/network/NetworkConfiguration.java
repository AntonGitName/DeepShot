package ru.spbau.mit.antonpp.deepshot.network;

import android.util.Log;

/**
 * @author antonpp
 * @since 20/11/15
 */
public class NetworkConfiguration {

    public final static String DEFAULT_IP = "192.168.65.4";
    private static final String TAG = NetworkConfiguration.class.getName();
    public static String SERVER_IP;
    public static String SERVER_ADDRESS;
    public static String SERVER_PORT = "5242";

    public static String URL_GET_LIST_STYLES;
    public static String URL_GET_FILTER;

    public static String URL_GET_LIST_RESULTS;
    public static String URL_GET_RESULT;

    public static String URL_POST_IMAGE;

    static {
        resetIp(DEFAULT_IP);
    }

    public static void resetIp(String ip) {

        Log.d(TAG, String.format("new ip:[%s]", ip));

        SERVER_IP = ip;
        SERVER_ADDRESS = "http://" + SERVER_IP + ":" + SERVER_PORT;

        URL_GET_LIST_STYLES = SERVER_ADDRESS + "/styles/list";
        URL_GET_FILTER = SERVER_ADDRESS + "/styles";

        URL_GET_LIST_RESULTS = SERVER_ADDRESS + "/results/list";
        URL_GET_RESULT = SERVER_ADDRESS + "/results";

        URL_POST_IMAGE = SERVER_ADDRESS + "/send";
    }

}
