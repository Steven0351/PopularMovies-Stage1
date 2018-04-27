package com.affinityforapps.popularmovies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.affinityforapps.popularmovies.model.Movie;
import com.affinityforapps.popularmovies.utils.MovieConsumer;
import com.affinityforapps.popularmovies.utils.MovieRequestor;
import com.affinityforapps.popularmovies.utils.SortIdentifier;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieConsumer {

    private final static String TAG_RECYCLERVIEW_POSITION = "recyclerview_position";
    private final static String TAG_FETCHED_MOVIES = "fetched_movies";
    private final static String TAG_CURRENT_PAGE = "current_page";
    private final static String TAG_MAX_PAGES = "max_pages";
    private final static String TAG_SORT_IDENTIFIER = "sort_identifier";

    private MovieRequestor mMovieRequestor;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SortIdentifier mSortIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        populateUI(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.clearOnScrollListeners();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int[] itemPositions = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager())
                .findFirstVisibleItemPositions(null);

        outState.putInt(TAG_RECYCLERVIEW_POSITION, itemPositions[0]);
        outState.putParcelableArrayList(TAG_FETCHED_MOVIES, (ArrayList) mAdapter.getMovies());
        outState.putInt(TAG_CURRENT_PAGE, mMovieRequestor.getCurrentPage());
        outState.putInt(TAG_MAX_PAGES, mMovieRequestor.getMaxPages());
        outState.putSerializable(TAG_SORT_IDENTIFIER, mSortIdentifier);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMovieRequestor.setCurrentPage(0);

        switch (item.getItemId()) {
            case R.id.sort_top_rated:
                mSortIdentifier = SortIdentifier.TOP_RATED;
                break;
            case R.id.sort_popular:
                mSortIdentifier = SortIdentifier.POPULAR;
        }

        requestMoviesWithSortIdentifier(mSortIdentifier);
        return true;
    }

    private void populateUI(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.rv_movies);
        mAdapter = new MovieAdapter();

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieRequestor = new MovieRequestor(this, APIKey.KEY, this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL)) {
                    requestMoviesWithSortIdentifier(mSortIdentifier);
                }
            }
        });

        if (savedInstanceState != null) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            mAdapter.setMovies(savedInstanceState.<Movie>getParcelableArrayList(TAG_FETCHED_MOVIES));
            mMovieRequestor.setCurrentPage(savedInstanceState.getInt(TAG_CURRENT_PAGE));
            mMovieRequestor.setMaxPages(savedInstanceState.getInt(TAG_MAX_PAGES));
            mSortIdentifier = (SortIdentifier) savedInstanceState.getSerializable(TAG_SORT_IDENTIFIER);
            mRecyclerView.scrollToPosition(savedInstanceState.getInt(TAG_RECYCLERVIEW_POSITION));

        } else {
            mSortIdentifier = SortIdentifier.TOP_RATED;
            requestMoviesWithSortIdentifier(mSortIdentifier);
        }
    }

    private void requestMoviesWithSortIdentifier(SortIdentifier identifier) {
        int currentPage = mMovieRequestor.getCurrentPage();

        if (currentPage >= mMovieRequestor.getMaxPages()) {
            return;
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);

        mMovieRequestor.performMovieRequest(identifier,currentPage + 1);
    }


    //region MovieConsumer implementation
    @Override
    public void consumeMovies(List<Movie> movies) {
        switch (mMovieRequestor.getCurrentPage()) {
            case 1:
                mAdapter.setMovies(movies);
                break;
            default:
                mAdapter.appendMovies(movies);
                break;
        }

        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError(VolleyError error) {
        error.printStackTrace();
        Snackbar.make(findViewById(R.id.cl_home), R.string.network_error_message,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry_message, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestMoviesWithSortIdentifier(mSortIdentifier);
                    }
                })
                .show();

        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }
    //endregion

}

