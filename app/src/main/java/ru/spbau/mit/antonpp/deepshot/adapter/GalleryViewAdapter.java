package ru.spbau.mit.antonpp.deepshot.adapter;

import android.content.Context;
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
import ru.spbau.mit.antonpp.deepshot.network.model.ResultItem;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class GalleryViewAdapter extends ArrayAdapter<ResultItem> {

    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();

    private static final int LAYOUT_RESOURCE_ID = R.layout.view_gallery_item;
    private static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS =
            new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

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
            holder = new ViewHolder((ImageView) row.findViewById(R.id.gallery_image_item),
                    (TextView) row.findViewById(R.id.gallery_text_item));

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ResultItem item = getItem(position);
        holder.getTxtTitle().setText(item.getName());
        IMAGE_LOADER.displayImage(item.getUri(), holder.getImgIcon(), DISPLAY_IMAGE_OPTIONS);

        return row;
    }

    private static class ViewHolder {
        private ImageView imgIcon;
        private TextView txtTitle;

        public ViewHolder(ImageView imgIcon, TextView txtTitle) {
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
