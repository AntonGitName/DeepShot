package ru.spbau.mit.antonpp.deepshot;

import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import ru.spbau.mit.antonpp.deepshot.fragment.CreatePaintingFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.GalleryFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.HelpPageFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.MainMenuFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.SettingsFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.ViewResultFragment;
import ru.spbau.mit.antonpp.deepshot.network.NetworkConfiguration;
import ru.spbau.mit.antonpp.deepshot.service.RegistrationIntentService;

public class MainMenuActivity
        extends AppCompatActivity
        implements MainMenuFragment.OnMainMenuOptionSelectedListener,
        GalleryFragment.OnResultImageClickedListener, NavigationView.OnNavigationItemSelectedListener {

    public final static String KEY_IP = "KEY_IP";
    public static final int PICK_FROM_CAMERA = 1;
    public static final int PICK_FROM_FILE = 2;
    public static final int SIGN_INTENT_RETURN_CODE = 3;
    private static final String TAG = MainMenuActivity.class.getName();
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String DEFAULT_USERNAME = "TEST_USERNAME";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            startFragment();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);

            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void startFragment() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, SettingsFragment.newInstance(), SettingsFragment.TAG).
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
            case SETTINGS:
                onSettingsButtonClicked();
                break;
            case HELP:
                onHelpButtonClicked();
                break;
            case EXIT:
                finish();
                break;
        }
    }

    private void onSettingsButtonClicked() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, SettingsFragment.newInstance(), SettingsFragment.TAG).
                addToBackStack(MainMenuFragment.TAG).
                commit();
    }

    public void onStyleChosen(long styleId) {
        final CreatePaintingFragment fragment = (CreatePaintingFragment) getSupportFragmentManager().findFragmentByTag(CreatePaintingFragment.TAG);
        if (fragment != null) {
            fragment.onStyleChosen(styleId);
        }
    }

    private void onConnectActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_INTENT_RETURN_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            MainApplication.getDataWrapper().setUsername(accountName);
            final SettingsFragment fragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);
            if (fragment != null) {
                fragment.updateUsername();
            }
        }
    }

    private void onChooseImageActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        final String imageUri = data.getData().toString();
        final CreatePaintingFragment fragment = (CreatePaintingFragment) getSupportFragmentManager().
                findFragmentByTag(CreatePaintingFragment.TAG);
        if (fragment != null) {
            fragment.onImageChosen(imageUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        switch (requestCode) {
            case PICK_FROM_CAMERA:
            case PICK_FROM_FILE:
                onChooseImageActivityResult(requestCode, resultCode, data);
                break;
            case SIGN_INTENT_RETURN_CODE:
                onConnectActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.getDataWrapper().setUsername(getLastUsername());
        NetworkConfiguration.resetIp(getLastIp());
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        MainApplication.getDataWrapper().saveState();
        saveLastIp();
        saveLastUsername();
        super.onStop();
    }

    @Override
    public void onResultImageClicked(String imageUrl) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, ViewResultFragment.newInstance(imageUrl), ViewResultFragment.TAG).
                addToBackStack(GalleryFragment.TAG).
                commit();
    }

    private String getLastIp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(KEY_IP, NetworkConfiguration.DEFAULT_IP);
    }

    private void saveLastIp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().
                putString(KEY_IP, NetworkConfiguration.SERVER_IP).
                apply();
    }

    private String getLastUsername() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(KEY_USERNAME, DEFAULT_USERNAME);
    }

    private void saveLastUsername() {
        final String username = MainApplication.getDataWrapper().getUsername();
        if (!username.equals(DEFAULT_USERNAME)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putString(KEY_USERNAME, username).apply();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(false);

        switch (id) {
            case R.id.navigation_gallery:
                onGalleryButtonClicked();
                break;
            case R.id.navigation_create:
                onCreateButtonClicked();
                break;
            case R.id.navigation_settings:
                onSettingsButtonClicked();
                break;
            case R.id.navigation_synchronize:
                break;
            default:
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
