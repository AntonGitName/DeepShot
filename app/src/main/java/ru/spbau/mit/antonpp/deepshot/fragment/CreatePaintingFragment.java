package ru.spbau.mit.antonpp.deepshot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.async.SendImageTask;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class CreatePaintingFragment extends Fragment {

    public static final String TAG = CreatePaintingFragment.class.getName();

    private ChooseStyleFragment chooseStyleFragment;
    private ChooseImageFragment chooseImageFragment;
    private ViewPager pager;
    private String imageUri;
    private Button nextButton;
    private Button prevButton;
    private long styleId;

    public CreatePaintingFragment() {
        // Required empty public constructor
    }

    public static CreatePaintingFragment newInstance() {
        return new CreatePaintingFragment();
    }

    private void onSend() {
        new SendImageTask(imageUri, styleId).execute();
        getFragmentManager().popBackStack();
    }

    public void onImageChosen(String uri) {
        imageUri = uri;
        nextButton.setEnabled(pager.getCurrentItem() == 0);
        chooseImageFragment.setImage(uri);
    }

    public void onStyleChosen(long id) {
        styleId = id;
        nextButton.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create, container, false);

        chooseImageFragment = ChooseImageFragment.newInstance();
        chooseStyleFragment = ChooseStyleFragment.newInstance();

        nextButton = (Button) rootView.findViewById(R.id.create_next_button);
        prevButton = (Button) rootView.findViewById(R.id.create_prev_button);

        pager = (ViewPager) rootView.findViewById(R.id.create_pager);
        pager.setAdapter(new CreatePaintingPageAdapter(getFragmentManager(), new Fragment[]{chooseImageFragment, chooseStyleFragment}));
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                prevButton.setEnabled(position != 0);
                nextButton.setEnabled(position == 0 && imageUri != null);
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
                if (pager.getCurrentItem() == 0) {
                    pager.setCurrentItem(1);
                } else {
                    onSend();
                }
            }
        });

        return rootView;
    }

    private static final class CreatePaintingPageAdapter extends FragmentStatePagerAdapter {

        private final Fragment[] fragments;

        public CreatePaintingPageAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
