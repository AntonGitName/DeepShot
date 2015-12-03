package ru.spbau.mit.antonpp.deepshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import ru.spbau.mit.antonpp.deepshot.MainApplication;
import ru.spbau.mit.antonpp.deepshot.R;
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class GalleryViewAdapter extends ArrayAdapter<ResultItem> {

    private static final int LAYOUT_RESOURCE_ID = R.layout.layout_gallery_item;

    private final LayoutInflater inflater;

    public GalleryViewAdapter(Context context) {
        super(context, LAYOUT_RESOURCE_ID);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<ResultItem> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            row = inflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
            holder = new ViewHolder((GifImageView) row.findViewById(R.id.gallery_image_item));

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ResultItem item = getItem(position);
        MainApplication.displayImage(item.getUri(), holder.getImgIcon());

        return row;
    }

    private static class ViewHolder {
        private GifImageView imgIcon;

        public ViewHolder(GifImageView imgIcon) {
            this.imgIcon = imgIcon;
        }

        public GifImageView getImgIcon() {
            return imgIcon;
        }
    }
}
