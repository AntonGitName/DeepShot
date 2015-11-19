package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

    public static String getJSONStringFromUrl(String url) {
        HttpURLConnection httpURLConnection = null;
        String jsonString = null;

        try {
            final URL u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            jsonString = getResponse(httpURLConnection);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return jsonString;
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
        Log.d("RESPONSE", stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String getImageUri(Context context, ImageType type, long id) {
        final SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(createFilename(type, id), null);
    }

    public static void saveImageUri(Context context, ImageType type, long id, String uri) {
        final SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(createFilename(type, id), uri).apply();
    }

    public static String createFilename(ImageType type, long id) {
        return String.format("%s%d.jpg", type, id);
    }

    public static String saveImage(ImageType type, Context context, long id, String encodedImage) {
        String filename = createFilename(type, id);
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

    public static boolean checkUriValid(String uri) {
        try {
            return uri != null && new File(URI.create(uri).getPath()).exists();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static String sendPOST(String url, List<NameValuePair> params) {
        HttpURLConnection httpURLConnection = null;
        String result = null;

        try {
            final URL u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            final OutputStream os = httpURLConnection.getOutputStream();
            final BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();

            result = getResponse(httpURLConnection);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    public static void sendImage(String username, String encodedImage, long styleId) {
        List<NameValuePair> params = new ArrayList<>();
        addParam(params, "username", username);
        addParam(params, "encodedImage", encodedImage);
        addParam(params, "styleId", styleId);
        sendPOST(Constants.URL_POST_IMAGE, params);
    }

    private static void addParam(List<NameValuePair> params, String key, Object value) {
        if (value != null) {
            params.add(new NameValuePair(key, value.toString()));
        }
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        final StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public enum ImageType {
        RESULT, STYLE
    }

    private static class NameValuePair {
        private final String name;
        private final String value;

        public NameValuePair(String name, String value) {

            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
