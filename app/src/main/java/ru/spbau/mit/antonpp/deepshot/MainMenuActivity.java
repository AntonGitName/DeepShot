package ru.spbau.mit.antonpp.deepshot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.spbau.mit.antonpp.deepshot.fragment.CreatePaintingFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.GalleryFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.HelpPageFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.MainMenuFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.ViewResultFragment;

public class MainMenuActivity extends AppCompatActivity
        implements MainMenuFragment.OnMainMenuOptionSelectedListener,
        GalleryFragment.OnResultImageClickedListener {

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

    private void onCreateButtonClicked() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, CreatePaintingFragment.newInstance(), CreatePaintingFragment.TAG).
                addToBackStack(MainMenuFragment.TAG).
                commit();
    }

    private void onHelpButtonClicked() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, HelpPageFragment.newInstance(), HelpPageFragment.TAG).
                addToBackStack(MainMenuFragment.TAG).
                commit();
    }

    private void onGalleryButtonClicked() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, GalleryFragment.newInstance(), GalleryFragment.TAG).
                addToBackStack(MainMenuFragment.TAG).
                commit();
    }

    @Override
    public void onMainMenuOptionSelected(MainMenuFragment.MainMenuOption option) {
        switch (option) {
            case CREATE:
                onCreateButtonClicked();
                break;
            case GALLERY:
                onGalleryButtonClicked();
                break;
            case HELP:
                onHelpButtonClicked();
                break;
            case EXIT:
                finish();
                break;
        }
    }

    public void onStyleChosen(long styleId) {
        final CreatePaintingFragment fragment = (CreatePaintingFragment) getSupportFragmentManager().findFragmentByTag(CreatePaintingFragment.TAG);
        if (fragment != null) {
            fragment.onStyleChosen(styleId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        final String imageUri = data.getData().toString();

        final CreatePaintingFragment fragment = (CreatePaintingFragment) getSupportFragmentManager().findFragmentByTag(CreatePaintingFragment.TAG);
        if (fragment != null) {
            fragment.onImageChosen(imageUri);
        }
    }

    @Override
    public void onResultImageClicked(String imageUrl) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, ViewResultFragment.newInstance(imageUrl), ViewResultFragment.TAG).
                addToBackStack(GalleryFragment.TAG).
                commit();
    }
}
