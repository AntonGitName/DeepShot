package ru.spbau.mit.antonpp.deepshot.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.network.FilterItem;
import ru.spbau.mit.antonpp.deepshot.network.FilterListLoader;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class FilterListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<FilterItem>> {

    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();
    private static final int MAX_HEIGHT = 200;

    private FilterListAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setDefaultEmptyText();

        adapter = new FilterListAdapter(getActivity());
        setListAdapter(adapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    private void setDefaultEmptyText() {
        setEmptyText("No filters found");
    }

    @Override
    public Loader<List<FilterItem>> onCreateLoader(int id, Bundle args) {
        return new FilterListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<FilterItem>> loader, List<FilterItem> data) {
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        if (data == null || data.isEmpty()) {
            Exception ex = ((FilterListLoader) loader).getException();
            if (ex != null) {
                setEmptyText(((FilterListLoader) loader).getException().getMessage());
            } else {
                setDefaultEmptyText();
            }
        } else {
            adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FilterItem>> loader) {
        adapter.setData(null);
    }

    public static class FilterListAdapter extends ArrayAdapter<FilterItem> {

        private static final int LAYOUT_RESOURCE_ID = R.layout.listview_item_row;
        private static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS =
                new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

        private final LayoutInflater inflater;

        public FilterListAdapter(Context context) {
            super(context, LAYOUT_RESOURCE_ID);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<FilterItem> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            final ListItemHolder holder;

            if (row == null) {
                row = inflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
                holder = new ListItemHolder((ImageView) row.findViewById(R.id.img_icon),
                        (TextView) row.findViewById(R.id.txt_title));

                row.setTag(holder);
            } else {
                holder = (ListItemHolder) row.getTag();
            }

            final FilterItem item = getItem(position);
            holder.getTxtTitle().setText(item.getName());
            IMAGE_LOADER.displayImage(item.getUri(), holder.getImgIcon(), DISPLAY_IMAGE_OPTIONS);

            return row;
        }

        private static class ListItemHolder {
            private ImageView imgIcon;
            private TextView txtTitle;

            public ListItemHolder(ImageView imgIcon, TextView txtTitle) {
                this.imgIcon = imgIcon;
                this.txtTitle = txtTitle;
            }

            public ImageView getImgIcon() {
                return imgIcon;
            }

            public TextView getTxtTitle() {
                return txtTitle;
            }
        }
    }
}
