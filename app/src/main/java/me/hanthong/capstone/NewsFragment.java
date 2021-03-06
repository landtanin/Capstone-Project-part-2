package me.hanthong.capstone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.hanthong.capstone.data.NewsColumns;
import me.hanthong.capstone.data.NewsProvider;
import me.hanthong.capstone.sync.SyncUtils;


public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int NEWS_LOADER = 0;
    protected RecyclerView mRecyclerView;
    protected TextView mEmptyView;
    protected MyNewsRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeContainer;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        //for crate home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                SyncUtils.TriggerRefresh();
                //mLoaderManager.restartLoader(NEWS_LOADER,null,null);
            }

        });



        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);

        mEmptyView = (TextView) view.findViewById(R.id.show_net_text);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MyNewsRecyclerViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager LoaderManager = getLoaderManager();
        LoaderManager.initLoader(NEWS_LOADER, null, this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            getActivity().startActivity(intent);
            return true;
        }else if(id == R.id.action_readlists){
            Intent intent = new Intent(getActivity(), ReadLists.class);
            getActivity().startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

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
        mSwipeContainer.setRefreshing(false);

        if(data.getCount()==0) {
            mSwipeContainer.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }else{
            mSwipeContainer.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
