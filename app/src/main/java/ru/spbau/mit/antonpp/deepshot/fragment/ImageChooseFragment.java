package ru.spbau.mit.antonpp.deepshot.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.spbau.mit.antonpp.deepshot.MainActivity;
import ru.spbau.mit.antonpp.deepshot.R;

public class ImageChooseFragment extends DialogFragment {

    public static final String TAG = ImageChooseFragment.class.getName();

    String mCurrentPhotoPath;

    public ImageChooseFragment() {
        // Required empty public constructor
    }

    public static ImageChooseFragment newInstance() {
        return new ImageChooseFragment();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:/" + image.getAbsolutePath();
        galleryAddPic();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Resources resources = getResources();
        final String[] items = new String[]{
                resources.getString(R.string.image_choose_camera),
                resources.getString(R.string.image_choose_sd)};

        final Activity activity = getActivity();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item, items);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(resources.getString(R.string.dialog_choose_image));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            ((MainActivity) activity).setCameraImageUri(mCurrentPhotoPath);
                            activity.startActivityForResult(takePictureIntent, MainActivity.PICK_FROM_CAMERA);
                        }
                    }
                } else {
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    activity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), MainActivity.PICK_FROM_FILE);
                }
            }
        });
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // no idea why does it work like this...
        if (getShowsDialog()) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return inflater.inflate(R.layout.fragment_image_choose, container, false);
    }
}
