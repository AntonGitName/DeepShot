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
 * @since 19/10/15
 */
public class PostImageTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = PostImageTask.class.getName();
    private static final String POST_ADDRESS = "http://192.168.67.17:8080/task";

    private final Bitmap imageToSend;
    private final Context context;
    private final OnImageReceiveHandler receiveHandler;


    public PostImageTask(Bitmap imageToSend, Context context, OnImageReceiveHandler receiveHandler) {
        this.imageToSend = imageToSend;
        this.context = context;
        this.receiveHandler = receiveHandler;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final String encodedString = Util.encodeImage(imageToSend);

        Ion.with(context)
                .load("POST", POST_ADDRESS)
                .setBodyParameter("encodedImage", encodedString)
                .setBodyParameter("filterId", encodedString)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                        final long taskId = result.get("id").getAsLong();
                        receiveHandler.onImageReceive(taskId);
                    }
                });

        return null;
    }

    public interface OnImageReceiveHandler {
        void onImageReceive(long taskId);
    }
}
