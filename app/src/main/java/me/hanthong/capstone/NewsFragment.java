package me.hanthong.capstone;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.hanthong.capstone.data.NewsColumns;
import me.hanthong.capstone.data.NewsProvider;


public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int NEWS_LOADER = 0;
    protected RecyclerView mRecyclerView;
    protected MyNewsRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    String[] PROJECTION = {
            NewsColumns._ID,
            NewsColumns.TITLE,
            NewsColumns.DATE,
            NewsColumns.LINK,
            NewsColumns.PHOTO,
            NewsColumns.FAV
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        //for crate home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MyNewsRecyclerViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String order  = NewsColumns.DATE+" DESC";
        return new CursorLoader(getActivity(), NewsProvider.Lists.LISTS,PROJECTION,null,null,order);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
