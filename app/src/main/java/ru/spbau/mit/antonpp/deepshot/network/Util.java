package ru.spbau.mit.antonpp.deepshot.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.antonpp.deepshot.Constants;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

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

    public static String sendGET(String getUrl, String key, String value) throws IOException, JSONException {
        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new NameValuePair(key, value));
        return sendGET(getUrl, params);
    }

    public static String sendGET(String getUrl) throws IOException, JSONException {
        return sendGET(getUrl, new ArrayList<NameValuePair>());
    }

    private static String sendGET(String getUrl, List<NameValuePair> params) throws IOException, JSONException {
        HttpURLConnection connection = null;
        String result = null;
        try {
            final URL url = new URL(getUrl + getGETQuery(params));
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            result = Util.getResponse(connection);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
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
            writer.write(getPOSTQuery(params));
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

    public static void sendImage(String username, String encodedImage, long styleId, String gcmRegistrationId) {
        List<NameValuePair> params = new ArrayList<>();
        addParam(params, "username", username);
        addParam(params, "encodedImage", encodedImage);
        addParam(params, "styleId", styleId);
        addParam(params, "gcmToken", gcmRegistrationId);
        sendPOST(NetworkConfiguration.URL_POST_IMAGE, params);
    }

    private static void addParam(List<NameValuePair> params, String key, Object value) {
        if (value != null) {
            params.add(new NameValuePair(key, value.toString()));
        }
    }

    private static String getGETQuery(List<NameValuePair> params) {
        final StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
                result.append("?");
            } else {
                result.append("&");
            }

            result.append(pair.getName());
            result.append("=");
            result.append(pair.getValue());
        }

        return result.toString();
    }

    private static String getPOSTQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
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

    public static StyleItem getStyle(Context context, long id) throws IOException, JSONException {
        final JSONObject jsonObject = new JSONObject(Util.sendGET(NetworkConfiguration.URL_GET_FILTER, "id", "" + id));
        StyleItem item = new StyleItem();
        item.setId(id);
        item.setName(jsonObject.getString(StyleItem.KEY_NAME));
        item.setUri(saveImage(ImageType.STYLE, context, id, jsonObject.getString(StyleItem.KEY_IMAGE)));
        return item;
    }

    public static ResultItem getResult(Context context, long id) throws IOException, JSONException {
        final JSONObject jsonObject = new JSONObject(Util.sendGET(NetworkConfiguration.URL_GET_RESULT, "id", "" + id));
        ResultItem item = new ResultItem();
        item.setId(id);
        item.setStatus(ResultItem.Status.valueOf(jsonObject.getString(ResultItem.KEY_STATUS)));
        if (item.getStatus() == ResultItem.Status.READY) {
            item.setUri(saveImage(ImageType.RESULT, context, id, jsonObject.getString(ResultItem.KEY_IMAGE)));
        } else {
            item.setUri(Constants.STUB_IMAGE);
        }
        return item;
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
