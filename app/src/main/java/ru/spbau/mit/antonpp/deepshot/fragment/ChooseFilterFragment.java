package ru.spbau.mit.antonpp.deepshot.fragment;

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
public class ChooseFilterFragment extends Fragment {

    public static final String TAG = ChooseFilterFragment.class.getName();

    public ChooseFilterFragment() {
        // Required empty public constructor
    }

    public static ChooseFilterFragment newInstance() {
        return new ChooseFilterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_choose_filter, container, false);
        return rootView;
    }
}
