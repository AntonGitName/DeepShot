package ru.spbau.mit.antonpp.deepshot.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.antonpp.deepshot.Constants;
import ru.spbau.mit.antonpp.deepshot.network.Util;
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class StyleListLoader extends AsyncTaskLoader<List<StyleItem>> {

    private static String TAG = "FilterListLoader";

    private Exception exception;

    public StyleListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<StyleItem> loadInBackground() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Util.getJSONStringFromUrl(Constants.URL_GET_LIST_STYLES));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        final int n = jsonArray.length();
        try {
            List<StyleItem> fileItemList = new ArrayList<>(n);
            for (int i = 0; i < n; ++i) {
                final long styleId = jsonArray.getLong(i);

                // TODO caching
//                String styleImageUri = Util.getImageUri(getContext(), Util.ImageType.STYLE, styleId);
//                if (!Util.checkUriValid(styleImageUri)) {
                URL url = new URL(Constants.URL_GET_FILTER + "?id=" + styleId);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");

                    JSONObject jsonObject = new JSONObject(Util.getResponse(connection));
                String styleImageUri = Util.saveImage(Util.ImageType.STYLE, getContext(), styleId, jsonObject.getString("preview"));
                String styleName = jsonObject.getString("name");
                Util.saveImageUri(getContext(), Util.ImageType.STYLE, styleId, styleImageUri);
//                }

                fileItemList.add(new StyleItem(styleName, styleId, styleImageUri));
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
