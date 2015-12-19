package ru.spbau.mit.antonpp.deepshot;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import ru.spbau.mit.antonpp.deepshot.async.UpdateDataTask;
import ru.spbau.mit.antonpp.deepshot.fragment.CreatePaintingFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.GalleryGridFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.GalleryListFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.HelpPageFragment;
import ru.spbau.mit.antonpp.deepshot.fragment.SettingsFragment;
import ru.spbau.mit.antonpp.deepshot.network.NetworkConfiguration;
import ru.spbau.mit.antonpp.deepshot.service.RegistrationIntentService;

public class MainActivity
        extends AppCompatActivity
        implements GalleryGridFragment.OnResultImageClickedListener,
        NavigationView.OnNavigationItemSelectedListener {

    public final static String KEY_IP = "KEY_IP";
    public static final int PICK_FROM_CAMERA = 1;
    public static final int PICK_FROM_FILE = 2;
    public static final int SIGN_INTENT_RETURN_CODE = 3;
    private static final String TAG = MainActivity.class.getName();
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver registrationBroadcastReceiver;

    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            onGalleryButtonClicked();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                final boolean sentToken = sharedPreferences
                        .getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
            }
        };

        loadSettings();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        if (MainApplication.getDataWrapper().getUsername().equals(MainApplication.DEFAULT_USERNAME)) {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, null,
                    false, null, null, null, null);
            startActivityForResult(intent, MainActivity.SIGN_INTENT_RETURN_CODE);
        }
    }

    private void onCreateButtonClicked() {
        pushFragment(CreatePaintingFragment.newInstance(), CreatePaintingFragment.TAG);
    }

    private void onHelpButtonClicked() {
        pushFragment(HelpPageFragment.newInstance(), HelpPageFragment.TAG);
    }

    public void onGalleryButtonClicked() {
        pushFragment(GalleryGridFragment.newInstance(), GalleryGridFragment.TAG);
    }

    private void onSettingsButtonClicked() {
        pushFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
    }

    private void onSynchronizationButtonClicked() {
        onGalleryButtonClicked();
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Synchronization with server...", "Please wait", true);
        new UpdateDataTask(progressDialog, this).execute();
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
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();
    }

    private void pushFragment(Fragment fragment, String tag) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, tag);
        if (!getCurrentFragmentTag().equals(tag)) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    @Override
    public void onResultImageClicked(int index) {
        pushFragment(GalleryListFragment.newInstance(index), GalleryListFragment.TAG);
    }

    private void loadSettings() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        MainApplication.getDataWrapper().setUsername(sp.getString(KEY_USERNAME, MainApplication.DEFAULT_USERNAME));
        NetworkConfiguration.resetIp(sp.getString(KEY_IP, NetworkConfiguration.DEFAULT_IP));
    }

    private void saveSettings() {
        MainApplication.getDataWrapper().saveState();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().
                putString(KEY_IP, NetworkConfiguration.SERVER_IP).
                apply();
        final String username = MainApplication.getDataWrapper().getUsername();
        if (!username.equals(MainApplication.DEFAULT_USERNAME)) {
            preferences.edit().putString(KEY_USERNAME, username).apply();
        }
    }

    private String getCurrentFragmentTag() {
        final FragmentManager fm = getSupportFragmentManager();
        final int n = fm.getBackStackEntryCount();
        final String tag;
        if (n != 0) {
            tag = fm.getBackStackEntryAt(n - 1).getName();
        } else {
            tag = "";
        }
        Log.d(TAG, tag);
        return tag;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if (getCurrentFragmentTag().length() == 0) {
                super.onBackPressed();
            }
            updateDrawer();
        }
    }

    private void updateDrawer() {
        final String tag = getCurrentFragmentTag();
        int index = -1;
        if (GalleryGridFragment.TAG.equals(tag)) {
            index = 0;
        } else if (GalleryListFragment.TAG.equals(tag)) {
            index = 0;
        } else if (SettingsFragment.TAG.equals(tag)) {
            index = 2;
        } else if (HelpPageFragment.TAG.equals(tag)) {
            index = 3;
        } else if (CreatePaintingFragment.TAG.equals(tag)) {
            index = 1;
        }
        if (index != -1) {
            navigationView.getMenu().getItem(index).setChecked(true);
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
        final int id = item.getItemId();
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
            case R.id.navigation_help:
                onHelpButtonClicked();
                break;
            case R.id.navigation_synchronize:
                onSynchronizationButtonClicked();
                break;
            default:
                return true;
        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
