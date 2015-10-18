package ru.spbau.mit.antonpp.deepshot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.spbau.mit.antonpp.deepshot.fragments.ImageChooseFragment;

public class MainActivity extends AppCompatActivity implements SendTask.OnImageReceiveHandler {

    private static final String IMAGE_URI = "IMAGE_URI";

    private final ImageLoader imageLoader = ImageLoader.getInstance();

    private ImageView mImageView;
    private Button sendButton;
    private ImageChooseFragment imageChooseFragment;
    private String imageUri;
    private Bitmap imageToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.iv_pic);
        sendButton = (Button) findViewById(R.id.btn_send);

        findViewById(R.id.btn_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooseFragment = ImageChooseFragment.newInstance();
                imageChooseFragment.show(getFragmentManager(), ImageChooseFragment.TAG);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTask(imageToSend, MainActivity.this, MainActivity.this).execute();
            }
        });

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getString(IMAGE_URI);
            imageLoader.displayImage(imageUri, mImageView);
            imageToSend = imageLoader.loadImageSync(imageUri);
            sendButton.setEnabled(true);
        } else {
            imageToSend = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(IMAGE_URI, imageUri);
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
        imageToSend = imageLoader.loadImageSync(imageUri);
        sendButton.setEnabled(true);
    }

    @Override
    public void onImageReceive(Bitmap image) {
        mImageView.setImageBitmap(image);
        imageToSend = image;
    }
}
