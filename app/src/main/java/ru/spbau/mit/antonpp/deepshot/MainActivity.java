package ru.spbau.mit.antonpp.deepshot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.spbau.mit.antonpp.deepshot.fragments.ImageChooseFragment;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_URI = "IMAGE_URI";
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView mImageView;
    private ImageChooseFragment imageChooseFragment;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.iv_pic);

        findViewById(R.id.btn_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooseFragment = ImageChooseFragment.newInstance();
                imageChooseFragment.show(getFragmentManager(), ImageChooseFragment.TAG);
            }
        });

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getString(IMAGE_URI);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(IMAGE_URI, imageUri);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (imageUri != null) {
            imageLoader.displayImage(imageUri, mImageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ImageChooseFragment.PICK_FROM_CAMERA) {
            imageUri = imageChooseFragment.getCameraImageUri().toString();
        } else {
            imageUri = data.getData().toString();
        }

        imageLoader.displayImage(imageUri, mImageView);
    }
}
