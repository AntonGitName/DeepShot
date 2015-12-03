package ru.spbau.mit.antonpp.deepshot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.droidsonroids.gif.GifImageView;
import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;

/**
 * @author antonpp
 * @since 19/11/15
 */
public class GalleryListFragment extends Fragment {

    public static final String TAG = GalleryListFragment.class.getName();
    private static final String KEY_INDEX = "KEY_INDEX";

    public GalleryListFragment() {
        // Required empty public constructor
    }

    public static GalleryListFragment newInstance(int index) {
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_INDEX, index);
        GalleryListFragment fragment = new GalleryListFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_result_image, container, false);
        final ViewPager pager = (ViewPager) rootView.findViewById(R.id.result_pager);
        pager.setAdapter(new PageAdapter(getFragmentManager()));
        pager.setCurrentItem(getArguments().getInt(KEY_INDEX));
        return rootView;
    }

    public static final class PageFragment extends Fragment {
        static final String KEY_URI = "arg_page_number";

        public static PageFragment newInstance(ResultItem item) {
            PageFragment pageFragment = new PageFragment();
            Bundle arguments = new Bundle();
            arguments.putString(KEY_URI, item.getUri());
            pageFragment.setArguments(arguments);
            return pageFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View page = inflater.inflate(R.layout.layout_gallery_single_item, null);
            final GifImageView imageView = (GifImageView) page.findViewById(R.id.gallery_image_item);
            MainApplication.displayImage(getArguments().getString(KEY_URI), imageView);
            return page;
        }
    }

    private static final class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(MainApplication.getDataWrapper().getResultItems().get(position));
        }

        @Override
        public int getCount() {
            return MainApplication.getDataWrapper().getResultItems().size();
        }
    }
}
