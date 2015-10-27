package ru.spbau.mit.antonpp.deepshot.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class Util {
    private Util() {
        throw new UnsupportedOperationException();
    }

    public static String encodeImage(Bitmap image) {
        final int COMPRESSION_QUALITY = 100;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
