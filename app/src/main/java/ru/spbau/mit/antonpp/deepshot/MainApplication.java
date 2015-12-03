package ru.spbau.mit.antonpp.deepshot;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import ru.spbau.mit.antonpp.deepshot.network.DataWrapper;

/**
 * @author antonpp
 * @since 04/10/15
 */
public class MainApplication extends Application {

    private static final String DEFAULT_USERNAME = "TEST_USERNAME";
    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();
    private static DataWrapper dataWrapper;

    public static DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    public static Bitmap loadImageSync(String uri) {
        return IMAGE_LOADER.loadImageSync(uri);
    }

    public static void displayImage(String uri, ImageView imageView) {
        if (!uri.equals(Constants.STUB_IMAGE)) {
            IMAGE_LOADER.displayImage(uri, imageView);
        } else {
            imageView.setImageResource(Constants.STUB_IMAGE_LARGE_ID);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dataWrapper = DataWrapper.getInstance(getApplicationContext(), DEFAULT_USERNAME);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        IMAGE_LOADER.init(config);
    }
}
