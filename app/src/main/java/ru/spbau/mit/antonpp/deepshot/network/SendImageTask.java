package ru.spbau.mit.antonpp.deepshot.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.spbau.mit.antonpp.deepshot.MyApplication;

/**
 * @author antonpp
 * @since 19/10/15
 */
public class SendImageTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SendImageTask.class.getName();

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private final String imageUrl;
    private final long styleId;


    public SendImageTask(String imageUrl, long styleId) {
        this.imageUrl = imageUrl;
        this.styleId = styleId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Bitmap image = imageLoader.loadImageSync(imageUrl);
        String encodedImage = Util.encodeImage(image);
        Util.sendImage(MyApplication.getUsername(), encodedImage, styleId);
        return null;
    }
}
