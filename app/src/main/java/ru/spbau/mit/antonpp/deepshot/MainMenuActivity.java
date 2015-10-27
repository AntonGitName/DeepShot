package ru.spbau.mit.antonpp.deepshot;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.spbau.mit.antonpp.deepshot.fragments.HelpPageFragment;
import ru.spbau.mit.antonpp.deepshot.fragments.ImageChooseFragment;
import ru.spbau.mit.antonpp.deepshot.fragments.MainMenuFragment;

public class MainMenuActivity extends AppCompatActivity implements MainMenuFragment.OnMainMenuOptionSelectedListener {

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

        if (savedInstanceState == null) {
            startFragment();
        }
    }

    private void startFragment() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, MainMenuFragment.newInstance(), MainMenuFragment.TAG).
                commit();
    }


    private void onHelpButtonClicked() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, HelpPageFragment.newInstance(), HelpPageFragment.TAG).
                addToBackStack(MainMenuFragment.TAG).
                commit();
    }

    @Override
    public void onMainMenuOptionSelected(MainMenuFragment.MainMenuOption option) {
        switch (option) {
            case CREATE:
                break;
            case GALLERY:
                break;
            case HELP:
                onHelpButtonClicked();
                break;
            case EXIT:
                finish();
                break;
        }
    }
}
