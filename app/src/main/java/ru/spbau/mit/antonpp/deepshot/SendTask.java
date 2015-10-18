package ru.spbau.mit.antonpp.deepshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;

/**
 * @author antonpp
 * @since 19/10/15
 */
public class SendTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SendTask.class.getName();
    private static final String POST_ADDRESS = "http://192.168.1.19:8080/task";

    private final Bitmap imageToSend;
    private final Context context;
    private final OnImageReceiveHandler receiveHandler;
    private Bitmap imageReceived;


    public SendTask(Bitmap imageToSend, Context context, OnImageReceiveHandler receiveHandler) {
        this.imageToSend = imageToSend;
        this.context = context;
        this.receiveHandler = receiveHandler;
    }

    private static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private static Bitmap getBitmapFromString(String encodedImage) {
        byte[] b = Base64.decode(encodedImage, 0);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final String encodedString = getStringFromBitmap(imageToSend);

        final JsonObject json = new JsonObject();
        json.addProperty("image", encodedString);

        Ion.with(context)
                .load("POST", POST_ADDRESS)
                .setBodyParameter("image", encodedString)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                        }
                        final Bitmap bitmap = getBitmapFromString(result.get("content").getAsString());
                        receiveHandler.onImageReceive(bitmap);
                    }
                });

        return null;
    }

    public interface OnImageReceiveHandler {
        void onImageReceive(Bitmap image);
    }
}
