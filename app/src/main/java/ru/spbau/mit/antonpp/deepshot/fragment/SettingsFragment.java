package ru.spbau.mit.antonpp.deepshot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;

import ru.spbau.mit.antonpp.deepshot.MainActivity;
import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.network.NetworkConfiguration;

/**
 * @author antonpp
 * @since 20/11/15
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = SettingsFragment.class.getName();

    private EditText editIpText;
    private EditText editPortText;
    private TextView serverIp;
    private TextView serverPort;
    private TextView username;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView.findViewById(R.id.btn_clear_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication.getDataWrapper().clearCache();
            }
        });

        editIpText = (EditText) rootView.findViewById(R.id.server_ip_edittext);
        editPortText = (EditText) rootView.findViewById(R.id.server_port_edittext);
        serverIp = (TextView) rootView.findViewById(R.id.text_server);
        serverPort = (TextView) rootView.findViewById(R.id.text_server_port);
        username = (TextView) rootView.findViewById(R.id.text_username);

        updateUsername();
        updateServerIp();

        rootView.findViewById(R.id.btn_set_server_ip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newIP = editIpText.getText().toString();
                MainApplication.getDataWrapper().clearCache();
                NetworkConfiguration.resetIp(newIP);
                updateServerIp();
            }
        });

        rootView.findViewById(R.id.btn_set_server_port).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPort = editPortText.getText().toString();
                MainApplication.getDataWrapper().clearCache();
                NetworkConfiguration.resetPort(newPort);
                updateServerIp();
            }
        });

        rootView.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = AccountPicker.newChooseAccountIntent(null, null, null,
                        false, null, null, null, null);
                getActivity().startActivityForResult(intent, MainActivity.SIGN_INTENT_RETURN_CODE);

            }
        });

        return rootView;
    }

    public void updateUsername() {
        this.username.setText(MainApplication.getDataWrapper().getUsername());
    }

    public void updateServerIp() {
        serverIp.setText(NetworkConfiguration.SERVER_IP);
        serverPort.setText(NetworkConfiguration.SERVER_PORT);
    }
}