package ru.spbau.mit.antonpp.deepshot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.spbau.mit.antonpp.deepshot.fragments.ImageChooseFragment;

public class MainActivity extends AppCompatActivity {

    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView mImageView;
    private ImageChooseFragment imageChooseFragment;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        final String uri;
        if (requestCode == ImageChooseFragment.PICK_FROM_CAMERA) {
            uri = imageChooseFragment.getCameraImageUri().toString();
        } else {
            uri = data.getData().toString();
        }

        imageLoader.displayImage(uri, mImageView);
    }
}
