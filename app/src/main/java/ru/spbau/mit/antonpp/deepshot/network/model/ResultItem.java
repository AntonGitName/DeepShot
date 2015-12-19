package ru.spbau.mit.antonpp.deepshot.network.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class ResultItem {

    public final static String KEY_ID = "id";
    public final static String KEY_STATUS = "status";
    public final static String KEY_URI = "uri";
    public final static String KEY_OWNER = "owner";
    public final static String KEY_IMAGE = "encodedImage";
    private static final String TAG = ResultItem.class.getName();

    private long id;
    private Status status;
    private String uri;
    private String owner;

    public static ResultItem fromJson(JSONObject jsonObject) throws JSONException {
        ResultItem resultItem = new ResultItem();
        resultItem.setId(jsonObject.getLong(KEY_ID));
        resultItem.setStatus(Status.valueOf(jsonObject.getString(KEY_STATUS)));
        resultItem.setUri(jsonObject.getString(KEY_URI));
        resultItem.setOwner(jsonObject.getString(KEY_OWNER));
        return resultItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
            jsonObject.put(KEY_OWNER, owner);
            jsonObject.put(KEY_STATUS, status.toString());
            jsonObject.put(KEY_URI, uri);

        } catch (JSONException e) {
            Log.e(TAG, "Could not convert ResultItem to JSON");
        }
        return jsonObject;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public enum Status {
        READY, PROCESSING, FAILED
    }
}