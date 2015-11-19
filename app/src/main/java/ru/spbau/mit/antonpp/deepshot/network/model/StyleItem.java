package ru.spbau.mit.antonpp.deepshot.network.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class StyleItem {

    public final static String KEY_ID = "id";
    public final static String KEY_NAME = "name";
    public final static String KEY_URI = "uri";
    public final static String KEY_IMAGE = "encodedImage";
    private static final String TAG = StyleItem.class.getName();
    private String name;
    private long id;
    private String uri;

    public StyleItem() {
    }

    public static StyleItem fromJson(JSONObject jsonObject) throws JSONException {
        StyleItem styleItem = new StyleItem();
        styleItem.setId(jsonObject.getLong(KEY_ID));
        styleItem.setName(jsonObject.getString(KEY_NAME));
        styleItem.setUri(jsonObject.getString(KEY_URI));
        return styleItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_ID, id);
            jsonObject.put(KEY_NAME, name);
            jsonObject.put(KEY_URI, uri);
        } catch (JSONException e) {
            Log.e(TAG, "Could not convert StyleItem to JSON");
        }
        return jsonObject;
    }
}
