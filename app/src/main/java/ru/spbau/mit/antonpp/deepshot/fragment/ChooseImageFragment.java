package ru.spbau.mit.antonpp.deepshot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.R;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class ChooseImageFragment extends Fragment {

    public static final String TAG = ChooseImageFragment.class.getName();

    private ImageView imageView;

    public ChooseImageFragment() {
        // Required empty public constructor
    }

    public static ChooseImageFragment newInstance() {
        return new ChooseImageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_choose_image, container, false);
        rootView.findViewById(R.id.create_choose_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageChooseFragment imageChooseFragment = ImageChooseFragment.newInstance();
                imageChooseFragment.show(getFragmentManager(), ImageChooseFragment.TAG);
            }
        });
        imageView = (ImageView) rootView.findViewById(R.id.create_image_view);
        return rootView;
    }

    public void setImage(String uri) {
        MainApplication.displayImage(uri, imageView);
    }
}
