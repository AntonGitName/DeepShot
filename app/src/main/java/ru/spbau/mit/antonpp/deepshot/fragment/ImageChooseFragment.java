package ru.spbau.mit.antonpp.deepshot.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ru.spbau.mit.antonpp.deepshot.R;

public class ImageChooseFragment extends DialogFragment {

    public static final String TAG = ImageChooseFragment.class.getName();

    public static final int PICK_FROM_CAMERA = 1;
    public static final int PICK_FROM_FILE = 2;

    public ImageChooseFragment() {
        // Required empty public constructor
    }

    public static ImageChooseFragment newInstance() {
        return new ImageChooseFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Resources resources = getResources();
        final String[] items = new String[]{
                resources.getString(R.string.image_choose_camera)
                , resources.getString(R.string.image_choose_sd)};

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
                        activity.startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
                    }
                } else {
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    activity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
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
