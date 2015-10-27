package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class GetFilterTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = PostImageTask.class.getName();
    private static final String GET_ADDRESS = "http://192.168.67.17:8080/filter";

    private final String filterName;
    private final Context context;
    private final OnFilterLoadedHandler onFilterLoadedHandler;

    public GetFilterTask(String filterName, Context context, OnFilterLoadedHandler onFilterLoadedHandler) {
        this.filterName = filterName;
        this.context = context;
        this.onFilterLoadedHandler = onFilterLoadedHandler;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Ion.with(context)
                .load("GET", GET_ADDRESS)
                .setBodyParameter("name", filterName)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                        final String name = result.get("name").getAsString();
                        final String encodedPreview = result.get("preview").getAsString();

                        final Bitmap preview = Util.decodeImage(encodedPreview);

                        onFilterLoadedHandler.onFilterLoaded(name, preview);
                    }
                });

        return null;
    }

    public interface OnFilterLoadedHandler {
        void onFilterLoaded(String filterName, Bitmap filterPreview);
    }
}
