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
import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.network.Util;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class GalleryLoader extends AsyncTaskLoader<List<ResultItem>> {

    private static String TAG = "GalleryLoader";
    private final String username;
    private Exception exception;

    public GalleryLoader(Context context, String username) {
        super(context);
        this.username = username;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ResultItem> loadInBackground() {
        // TODO
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Util.getJSONStringFromUrl(Constants.URL_GET_LIST_RESULTS + "?username=" + username));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        final int n = jsonArray.length();
        try {
            List<ResultItem> fileItemList = new ArrayList<>(n);

            for (int i = 0; i < n; ++i) {
                final long resultId = jsonArray.getLong(i);

//                String resultImageUri = Util.getImageUri(getContext(), Util.ImageType.RESULT, resultId);
//                if (!Util.checkUriValid(resultImageUri)) {
                URL url = new URL(Constants.URL_GET_RESULT + "?id=" + resultId);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");

                JSONObject jsonObject = new JSONObject(Util.getResponse(connection));
                Status status = Status.valueOf(jsonObject.getString("status"));
                String resultImageUri;
                String name;
                if (status == Status.READY) {
                    resultImageUri = Util.saveImage(Util.ImageType.RESULT,
                            getContext(),
                            resultId,
                            jsonObject.getString("encodedImage"));
                    Util.saveImageUri(getContext(), Util.ImageType.RESULT, resultId, resultImageUri);
                    name = "" + resultId;
                } else {
                    resultImageUri = "drawable://" + R.drawable.default_image;
                    name = status.toString();
                }
//                }

                fileItemList.add(new ResultItem(name, resultImageUri));
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

    public enum Status {
        READY, PROCESSING, FAILED
    }
}