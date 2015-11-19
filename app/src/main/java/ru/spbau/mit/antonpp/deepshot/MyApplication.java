package ru.spbau.mit.antonpp.deepshot;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @author antonpp
 * @since 04/10/15
 */
public class MyApplication extends Application {

    private static String username;

    public static String getUsername() {
        return username;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Account[] accounts = AccountManager.get(getApplicationContext()).
                getAccountsByType("com.google");
        if (accounts.length != 0) {
            username = accounts[0].name;
        } else {
            username = "emulator";
        }

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
