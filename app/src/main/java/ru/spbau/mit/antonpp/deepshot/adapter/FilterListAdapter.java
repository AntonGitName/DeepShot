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
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

/**
 * @author antonpp
 * @since 20/11/15
 */
public class FilterListAdapter extends ArrayAdapter<StyleItem> {

    private static final int LAYOUT_RESOURCE_ID = R.layout.listview_item_row;
    private static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS =
            new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();


    private final LayoutInflater inflater;

    public FilterListAdapter(Context context) {
        super(context, LAYOUT_RESOURCE_ID);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<StyleItem> data) {
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

        final StyleItem item = getItem(position);
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
