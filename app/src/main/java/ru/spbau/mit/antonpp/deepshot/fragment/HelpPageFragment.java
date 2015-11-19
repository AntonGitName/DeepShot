package ru.spbau.mit.antonpp.deepshot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.spbau.mit.antonpp.deepshot.R;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class HelpPageFragment extends Fragment {

    public static final String TAG = HelpPageFragment.class.getName();

    private static final int PAGE_STRINGS_RESOURCES_ID[] = {R.string.create_label, R.string.create_text,
            R.string.gallery_label, R.string.gallery_text, R.string.about_label, R.string.about_text};

    private static final int PAGES_COUNT = 3;
    private static final float TEXT_SIZE = 22f;

    public HelpPageFragment() {
        // Required empty public constructor
    }

    public static HelpPageFragment newInstance() {
        return new HelpPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_help_page, container, false);

        final ViewPager pager = (ViewPager) rootView.findViewById(R.id.help_pager);
        pager.setAdapter(new HelpPageAdapter(getFragmentManager()));

        return rootView;
    }

    public static final class PageFragment extends Fragment {
        static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

        private int pageNumber;

        static PageFragment newInstance(int page) {
            PageFragment pageFragment = new PageFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
            pageFragment.setArguments(arguments);
            return pageFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View page = inflater.inflate(R.layout.layout_help_page, null);

            TextView textView = (TextView) page.findViewById(R.id.help_page_label);
            textView.setText(PAGE_STRINGS_RESOURCES_ID[pageNumber * 2]);
            textView.setTextSize(TEXT_SIZE);
            textView = (TextView) page.findViewById(R.id.help_page_text);
            textView.setText(PAGE_STRINGS_RESOURCES_ID[pageNumber * 2 + 1]);
            textView.setTextSize(TEXT_SIZE);
            return page;
        }
    }

    private static final class HelpPageAdapter extends FragmentStatePagerAdapter {

        public HelpPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }
    }
}
