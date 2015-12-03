package ru.spbau.mit.antonpp.deepshot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import pl.droidsonroids.gif.GifImageView;
import ru.spbau.mit.antonpp.deepshot.Constants;
import ru.spbau.mit.antonpp.deepshot.R;

/**
 * @author antonpp
 * @since 19/11/15
 */
public class ViewResultFragment extends Fragment {

    public static final String TAG = ViewResultFragment.class.getName();
    private static final String IMAGE_URL = "IMAGE_URL";
    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();

    public ViewResultFragment() {
        // Required empty public constructor
    }

    public static ViewResultFragment newInstance(String imageUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(IMAGE_URL, imageUrl);
        ViewResultFragment fragment = new ViewResultFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_result_image, container, false);

        final GifImageView imageView = (GifImageView) rootView.findViewById(R.id.result_image_view);
        final String uri = getArguments().getString(IMAGE_URL);
        if (!uri.equals(Constants.STUB_IMAGE)) {
            IMAGE_LOADER.displayImage(uri, imageView);
        } else {
            imageView.setImageResource(Constants.STUB_IMAGE_ID);
        }
        return rootView;
    }

}
