package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import ru.spbau.mit.antonpp.deepshot.Constants;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class Util {

    private static final String TAG = Util.class.getName();

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

    public static JSONArray getJSONFromUrl(String url) {
        JSONArray jsonArray = null;
        HttpURLConnection httpURLConnection = null;
        String jsonString = null;

        try {
            URL u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            jsonString = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static String getFilterUri(Context context, String filterName) {
        final SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(filterName, null);
    }

    public static void saveFilterUri(Context context, String filterName, String uri) {
        final SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(filterName, uri).apply();
    }

    public static String saveImage(Context context, String filterName, String encodedImage) {
        String filename = filterName + ".jpg";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(Base64.decode(encodedImage, 0));
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return "file://" + context.getFilesDir().getPath() + File.separator + filename;
    }

    public static String getResponse(HttpURLConnection httpURLConnection) {
        final StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
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

    public static boolean checkUriValid(String uri) {
        return uri != null && new File(URI.create(uri).getPath()).exists();
    }
}
