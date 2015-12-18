package ru.spbau.mit.antonpp.deepshot.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.adapter.GalleryViewAdapter;
import ru.spbau.mit.antonpp.deepshot.async.GalleryLoader;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class GalleryGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ResultItem>> {

    public static final String TAG = GalleryGridFragment.class.getName();

    private GalleryViewAdapter adapter;
    private GridView gridView;
    private OnResultImageClickedListener onResultImageClickedListener;

    public GalleryGridFragment() {
        // Required empty public constructor
    }

    public static GalleryGridFragment newInstance() {
        return new GalleryGridFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new GalleryViewAdapter(getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onResultImageClickedListener.onResultImageClicked(i);
            }
        });
        gridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        TextView emptyView = (TextView) rootView.findViewById(android.R.id.empty);
        gridView.setEmptyView(emptyView);
        return rootView;
    }

    @Override
    public Loader<List<ResultItem>> onCreateLoader(int id, Bundle args) {
        return new GalleryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ResultItem>> loader, List<ResultItem> data) {
        if (data != null) {
            adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ResultItem>> loader) {
        adapter.setData(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onResultImageClickedListener = (OnResultImageClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMainMenuOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onResultImageClickedListener = null;
    }

    public interface OnResultImageClickedListener {
        void onResultImageClicked(int index);
    }
}
