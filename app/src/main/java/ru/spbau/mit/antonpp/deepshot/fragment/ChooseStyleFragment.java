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
public class ChooseStyleFragment extends Fragment {

    public static final String TAG = ChooseStyleFragment.class.getName();

    public ChooseStyleFragment() {
        // Required empty public constructor
    }

    public static ChooseStyleFragment newInstance() {
        return new ChooseStyleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_style, container, false);
    }
}
