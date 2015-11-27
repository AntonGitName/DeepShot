package ru.spbau.mit.antonpp.deepshot;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class Constants {

    public static final String CACHE_FILE = "cache.txt";
    public static final String STUB_IMAGE_URI = "drawable://" + R.drawable.stub_image;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    private Constants() {
        throw new UnsupportedOperationException();
    }
}
