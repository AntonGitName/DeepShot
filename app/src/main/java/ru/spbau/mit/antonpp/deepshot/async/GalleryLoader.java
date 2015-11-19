package ru.spbau.mit.antonpp.deepshot.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class GalleryLoader extends AsyncTaskLoader<List<ResultItem>> {

    private static String TAG = "GalleryLoader";

    public GalleryLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ResultItem> loadInBackground() {
        MainApplication.getDataWrapper().updateResults();
        return MainApplication.getDataWrapper().getResultItems();
    }
}