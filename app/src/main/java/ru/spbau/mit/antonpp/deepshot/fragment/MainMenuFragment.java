package ru.spbau.mit.antonpp.deepshot.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.spbau.mit.antonpp.deepshot.R;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class MainMenuFragment extends Fragment {

    public static final String TAG = "MainMenuFragment";

    private OnMainMenuOptionSelectedListener menuOptionSelectedListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        rootView.findViewById(R.id.about_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuOptionSelectedListener.onMainMenuOptionSelected(MainMenuOption.HELP);
            }
        });

        rootView.findViewById(R.id.quit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuOptionSelectedListener.onMainMenuOptionSelected(MainMenuOption.EXIT);
            }
        });

        rootView.findViewById(R.id.create_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuOptionSelectedListener.onMainMenuOptionSelected(MainMenuOption.CREATE);
            }
        });

        rootView.findViewById(R.id.gallery_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuOptionSelectedListener.onMainMenuOptionSelected(MainMenuOption.GALLERY);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            menuOptionSelectedListener = (OnMainMenuOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainMenuOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        menuOptionSelectedListener = null;
    }

    public enum MainMenuOption {
        CREATE,
        GALLERY,
        HELP,
        EXIT
    }

    public interface OnMainMenuOptionSelectedListener {
        void onMainMenuOptionSelected(MainMenuOption option);
    }

}
