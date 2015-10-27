package ru.spbau.mit.antonpp.deepshot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.spbau.mit.antonpp.deepshot.fragments.CreatePaintingFragment;
import ru.spbau.mit.antonpp.deepshot.fragments.HelpPageFragment;
import ru.spbau.mit.antonpp.deepshot.fragments.MainMenuFragment;

public class MainMenuActivity extends AppCompatActivity implements MainMenuFragment.OnMainMenuOptionSelectedListener {

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

    @Override
    public void onMainMenuOptionSelected(MainMenuFragment.MainMenuOption option) {
        switch (option) {
            case CREATE:
                onCreateButtonClicked();
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
