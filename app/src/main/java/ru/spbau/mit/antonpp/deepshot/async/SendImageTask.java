package ru.spbau.mit.antonpp.deepshot.async;

import android.os.AsyncTask;

import ru.spbau.mit.antonpp.deepshot.MainApplication;

/**
 * @author antonpp
 * @since 19/10/15
 */
public class SendImageTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SendImageTask.class.getName();

    private final String imageUrl;
    private final long styleId;


    public SendImageTask(String imageUrl, long styleId) {
        this.imageUrl = imageUrl;
        this.styleId = styleId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MainApplication.getDataWrapper().sendImage(imageUrl, styleId);
        return null;
    }
}
