package ru.spbau.mit.antonpp.deepshot.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class StyleListLoader extends AsyncTaskLoader<List<StyleItem>> {

    private static String TAG = "FilterListLoader";

    public StyleListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<StyleItem> loadInBackground() {
        MainApplication.getDataWrapper().updateStyles();
        return MainApplication.getDataWrapper().getStyleItems();
    }
}
