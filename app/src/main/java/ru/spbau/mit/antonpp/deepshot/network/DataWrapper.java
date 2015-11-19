package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.antonpp.deepshot.Constants;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

/**
 * @author antonpp
 * @since 19/11/15
 */
public class DataWrapper {

    private static final String TAG = DataWrapper.class.getName();

    private static DataWrapper instance;

    private final Context context;
    private List<ResultItem> resultItems;
    private List<StyleItem> styleItems;

    private DataWrapper(Context context) {
        this.context = context;
    }

    public DataWrapper getInstance(Context context) {
        return
    }

    public List<ResultItem> getResultItems() {
        return new ArrayList<>(resultItems);
    }

    public List<StyleItem> getStyleItems() {
        return new ArrayList<>(styleItems);
    }

    public void updateStyles() {

    }

    public void updateResults() {

    }

    public void saveState() throws JSONException {
        final JSONObject rootJson = new JSONObject();

        final JSONArray jsonArrayResults = new JSONArray();
        for (ResultItem resultItem : resultItems) {
            final JSONObject jsonObject = resultItem.toJsonObject();
            jsonArrayResults.put(jsonObject);
        }
        final JSONArray jsonArrayStyles = new JSONArray();
        for (StyleItem styleItem : styleItems) {
            final JSONObject jsonObject = styleItem.toJsonObject();
            jsonArrayStyles.put(jsonObject);
        }

        rootJson.put("results", jsonArrayResults);
        rootJson.put("styles", jsonArrayResults);

        final String jsonString = rootJson.toString();
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(Constants.CACHE_FILE, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void loadState() {

    }
}
