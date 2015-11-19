package ru.spbau.mit.antonpp.deepshot.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import ru.spbau.mit.antonpp.deepshot.MainMenuActivity;
import ru.spbau.mit.antonpp.deepshot.adapter.FilterListAdapter;
import ru.spbau.mit.antonpp.deepshot.async.StyleListLoader;
import ru.spbau.mit.antonpp.deepshot.network.model.StyleItem;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class StyleListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<StyleItem>> {

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
        setEmptyText("Could not load styles");
    }

    @Override
    public Loader<List<StyleItem>> onCreateLoader(int id, Bundle args) {
        return new StyleListLoader(getActivity());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ((MainMenuActivity) getActivity()).onStyleChosen(adapter.getItem(position).getId());
    }

    @Override
    public void onLoadFinished(Loader<List<StyleItem>> loader, List<StyleItem> data) {
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        if (data == null || data.isEmpty()) {
            setDefaultEmptyText();
        } else {
            adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<StyleItem>> loader) {
        adapter.setData(null);
    }
}
