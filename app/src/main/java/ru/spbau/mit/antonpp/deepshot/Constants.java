package ru.spbau.mit.antonpp.deepshot;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class Constants {

    public static final String CACHE_FILE = "cache.txt";
    public static final String LOADING_IMAGE = "drawable://" + R.drawable.loading;
    public static final String ERROR_IMAGE = "drawable://" + R.drawable.error_image;

    public static final int STUB_IMAGE_ERROR_ID = R.drawable.error_image;
    public static final int STUB_IMAGE_LOADING_ID = R.drawable.loading_large;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    private Constants() {
        throw new UnsupportedOperationException();
    }
}
