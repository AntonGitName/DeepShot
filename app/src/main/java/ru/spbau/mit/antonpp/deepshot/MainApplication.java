package ru.spbau.mit.antonpp.deepshot;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ru.spbau.mit.antonpp.deepshot.network.DataWrapper;

/**
 * @author antonpp
 * @since 04/10/15
 */
public class MainApplication extends Application {

    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String DEFAULT_USERNAME = "TEST_USERNAME";
    private static DataWrapper dataWrapper;

    public static DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dataWrapper = DataWrapper.getInstance(getApplicationContext(), DEFAULT_USERNAME);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
