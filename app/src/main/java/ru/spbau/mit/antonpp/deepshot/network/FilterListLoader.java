package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.antonpp.deepshot.Constants;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class FilterListLoader extends AsyncTaskLoader<List<FilterItem>> {

    private static String TAG = "FilterListLoader";

    private Exception exception;

    public FilterListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<FilterItem> loadInBackground() {
        JSONArray jsonArray = Util.getJSONFromUrl(Constants.URL_GET_LIST_FILTERS);
        final int n = jsonArray.length();
        try {
            List<FilterItem> fileItemList = new ArrayList<>(n);
            for (int i = 0; i < n; ++i) {
                final String filterName = jsonArray.getString(i);

                String filterImageUri = Util.getFilterUri(getContext(), filterName);
                if (!Util.checkUriValid(filterImageUri)) {
                    URL url = new URL(Constants.URL_GET_FILTER + "?name=" + filterName);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");

                    JSONObject jsonObject = new JSONObject(Util.getResponse(connection));
                    filterImageUri = Util.saveImage(getContext(), filterName, jsonObject.getString("preview"));
                    Util.saveFilterUri(getContext(), filterName, filterImageUri);
                }

                fileItemList.add(new FilterItem(filterName, filterImageUri));
            }
            return fileItemList;
        } catch (JSONException | IOException e) {
            exception = e;
        }
        return null;
    }

    public Exception getException() {
        return exception;
    }
}
