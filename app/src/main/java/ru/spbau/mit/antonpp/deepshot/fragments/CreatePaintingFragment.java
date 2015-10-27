package ru.spbau.mit.antonpp.deepshot.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.spbau.mit.antonpp.deepshot.R;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class CreatePaintingFragment extends Fragment {

    public static final String TAG = CreatePaintingFragment.class.getName();
    private static final int PAGE_STRINGS_RESOURCES_ID[] = {R.string.step1,
            R.string.create_choose1_button, R.string.step2, R.string.create_choose2_button};
    private static final int PAGES_COUNT = 2;
    private static final float TEXT_SIZE = 22f;
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private ViewPager pager;
    private Bitmap chosenImage;
    private Button nextButton;
    private Button prevButton;

    public CreatePaintingFragment() {
        // Required empty public constructor
    }

    public static CreatePaintingFragment newInstance() {
        return new CreatePaintingFragment();
    }

    public void onImageChosen(String uri) {
        ImageView imageView = (ImageView) pager.findViewById(R.id.create_image_view);
        imageLoader.displayImage(uri, imageView);
        chosenImage = imageLoader.loadImageSync(uri);
        nextButton.setEnabled(pager.getCurrentItem() == 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create, container, false);

        nextButton = (Button) rootView.findViewById(R.id.create_next_button);
        prevButton = (Button) rootView.findViewById(R.id.create_prev_button);

        pager = (ViewPager) rootView.findViewById(R.id.create_pager);
        pager.setAdapter(new HelpPageAdapter(getFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                prevButton.setEnabled(position != 0);
                nextButton.setEnabled(position == 0 && chosenImage != null);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });

        return rootView;
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
            final View page = inflater.inflate(R.layout.layout_create, null);

            final TextView textView = (TextView) page.findViewById(R.id.create_label);
            textView.setText(PAGE_STRINGS_RESOURCES_ID[pageNumber * 2]);
            textView.setTextSize(TEXT_SIZE);
            final Button button = (Button) page.findViewById(R.id.create_choose_button);
            button.setText(PAGE_STRINGS_RESOURCES_ID[pageNumber * 2 + 1]);
            button.setTextSize(TEXT_SIZE);

            if (pageNumber == 0) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ImageChooseFragment imageChooseFragment = ImageChooseFragment.newInstance();
                        imageChooseFragment.show(PageFragment.this.getActivity().getFragmentManager(), ImageChooseFragment.TAG);
                    }
                });
            }

            return page;
        }
    }

}
