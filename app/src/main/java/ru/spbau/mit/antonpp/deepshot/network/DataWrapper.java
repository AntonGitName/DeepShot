package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.antonpp.deepshot.Constants;
import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

/**
 * @author antonpp
 * @since 19/11/15
 */
public class DataWrapper {

    private static final String TAG = DataWrapper.class.getName();

    private final static String KEY_RESULTS = "KEY_RESULTS";
    private final static String KEY_STYLES = "KEY_STYLES";

    private static DataWrapper instance;

    private final Context context;
    private String username;
    private String gcmRegistrationId;
    private List<ResultItem> resultItems;
    private List<StyleItem> styleItems;
    private DataWrapper(Context context, String username) {
        this.context = context;
        this.username = username;
        loadState();
    }

    public static DataWrapper getInstance(Context context, String username) {
        if (instance == null) {
            instance = new DataWrapper(context, username);
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ResultItem> getResultItems() {
        return new ArrayList<>(resultItems);
    }

    public List<StyleItem> getStyleItems() {
        return new ArrayList<>(styleItems);
    }

    private ResultItem findResult(long id) {
        for (ResultItem resultItem : resultItems) {
            if (resultItem.getId() == id) {
                return resultItem;
            }
        }
        return null;
    }

    private boolean hasThisStyle(long styleId) {
        for (StyleItem styleItem : styleItems) {
            if (styleItem.getId() == styleId) {
                return true;
            }
        }
        return false;
    }

    public boolean updateStyles() {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(Util.sendGET(NetworkConfiguration.URL_GET_LIST_STYLES));
        } catch (JSONException | IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        final int n = jsonArray.length();
        boolean isOkay = true;
        for (int i = 0; i < n; ++i) {
            try {
                final long styleId = jsonArray.getLong(i);
                if (hasThisStyle(styleId)) {
                    continue;
                }

                final StyleItem styleItem = Util.getStyle(context, styleId);
                styleItems.add(styleItem);
            } catch (JSONException | IOException e) {
                Log.e(TAG, e.getMessage());
                isOkay = false;
            }
        }
        return isOkay;
    }

    public boolean updateResults() {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(Util.sendGET(NetworkConfiguration.URL_GET_LIST_RESULTS, "username", username));
        } catch (JSONException | IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        final int n = jsonArray.length();
        boolean isOkay = true;
        for (int i = 0; i < n; ++i) {
            try {
                final long id = jsonArray.getLong(i);
                final ResultItem item = findResult(id);
                if (item != null && item.getStatus() == ResultItem.Status.READY) {
                    continue;
                }

                final ResultItem resultItem = Util.getResult(context, id);
                if (item == null) {
                    resultItems.add(resultItem);
                } else if (item.getStatus() != resultItem.getStatus()) {
                    resultItems.remove(item);
                    resultItems.add(resultItem);
                }
            } catch (JSONException | IOException e) {
                Log.e(TAG, e.getMessage());
                isOkay = false;
            }
        }
        return isOkay;
    }

    public void saveState() {
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

        try {
            rootJson.put(KEY_RESULTS, jsonArrayResults);
            rootJson.put(KEY_STYLES, jsonArrayStyles);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to save cache!");
        }

        writeCacheFile(rootJson.toString());
    }

    private void writeCacheFile(String text) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(Constants.CACHE_FILE, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    private String readCacheFile() throws FileNotFoundException {
        FileInputStream inputStream = context.openFileInput(Constants.CACHE_FILE);
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
        return stringBuilder.toString();
    }

    public Exception loadState() {
        Exception exception = null;
        String cacheJson = null;
        try {
            cacheJson = readCacheFile();
        } catch (FileNotFoundException e) {
            Log.v(TAG, "Cache file not found");
            exception = e;
        }

        resultItems = new ArrayList<>();
        styleItems = new ArrayList<>();

        if (cacheJson == null) {
            return exception;
        }

        try {
            final JSONObject rootJson = new JSONObject(cacheJson);
            final JSONArray jsonArrayResults = rootJson.getJSONArray(KEY_RESULTS);
            final JSONArray jsonArrayStyles = rootJson.getJSONArray(KEY_STYLES);

            int n = jsonArrayResults.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject jsonObject = jsonArrayResults.getJSONObject(i);
                resultItems.add(ResultItem.fromJson(jsonObject));
            }

            n = jsonArrayStyles.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject jsonObject = jsonArrayStyles.getJSONObject(i);
                styleItems.add(StyleItem.fromJson(jsonObject));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Could not load cache file");
            exception = e;
        }
        return exception;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public void sendImage(String imageUrl, long styleId) {
        Bitmap image = MainApplication.loadImageSync(imageUrl);
        String encodedImage = Util.encodeImage(image);
        Util.sendImage(username, encodedImage, styleId, gcmRegistrationId);
    }

    public void clearCache() {
        resultItems = new ArrayList<>();
        styleItems = new ArrayList<>();
        final File cacheFile = new File("file://" + context.getFilesDir().getPath() + File.separator + Constants.CACHE_FILE);
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
    }

    public boolean sync() {
        return (updateStyles() && updateResults());
    }
}
