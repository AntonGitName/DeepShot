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

import ru.spbau.mit.antonpp.deepshot.MainActivity;
import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.async.SendImageTask;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class CreatePaintingFragment extends Fragment {

    public static final String TAG = CreatePaintingFragment.class.getName();
    private static final int PAGES_COUNT = 2;

    private ChooseStyleFragment chooseStyleFragment;
    private ChooseImageFragment chooseImageFragment;
    private ViewPager pager;
    private String imageUri = null;
    private Button nextButton;
    private Button prevButton;
    private Long styleId = null;

    public CreatePaintingFragment() {
        // Required empty public constructor
    }

    public static CreatePaintingFragment newInstance() {
        return new CreatePaintingFragment();
    }

    private void onSend() {
        new SendImageTask(imageUri, styleId).execute();
        ((MainActivity) getActivity()).onGalleryButtonClicked();
    }

    public void onImageChosen(String uri) {
        imageUri = uri;
        chooseImageFragment.setImage(uri);
        updateMoveButtons(pager.getCurrentItem());
    }

    public void onStyleChosen(long id) {
        styleId = id;
        updateMoveButtons(pager.getCurrentItem());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create, container, false);

        chooseImageFragment = ChooseImageFragment.newInstance();
        chooseStyleFragment = ChooseStyleFragment.newInstance();

        pager = (ViewPager) rootView.findViewById(R.id.create_pager);
        pager.setAdapter(new CreatePaintingPageAdapter(getFragmentManager(), new Fragment[]{chooseImageFragment, chooseStyleFragment}));
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateMoveButtons(position);
            }
        });

        prevButton = (Button) rootView.findViewById(R.id.prev_button);
        nextButton = (Button) rootView.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem() == PAGES_COUNT - 1) {
                    onSend();
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });

        updateMoveButtons(pager.getCurrentItem());

        return rootView;
    }

    private void updateMoveButtons(int position) {
        prevButton.setEnabled(position != 0);
        if (position == 0) {
            nextButton.setEnabled(imageUri != null);
        } else {
            nextButton.setEnabled(styleId != null);
        }
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
