package ru.spbau.mit.antonpp.deepshot;

import android.accounts.Account;
import android.accounts.AccountManager;
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

    private static DataWrapper dataWrapper;

    public static DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Account[] accounts = AccountManager.get(getApplicationContext()).
                getAccountsByType("com.google");
        final String username;
        if (accounts.length != 0) {
            username = accounts[0].name;
        } else {
            username = "emulator";
        }

        dataWrapper = DataWrapper.getInstance(getApplicationContext(), username);

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
