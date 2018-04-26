package com.affinityforapps.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.affinityforapps.popularmovies.model.Movie;
import com.affinityforapps.popularmovies.utils.MovieConsumer;
import com.affinityforapps.popularmovies.utils.MovieRequestor;
import com.affinityforapps.popularmovies.utils.SortIdentifier;
import com.android.volley.VolleyError;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieConsumer {

    private MovieRequestor mMovieRequestor;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = findViewById(R.id.rv_movies);
        mAdapter = new MovieAdapter();
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieRequestor = new MovieRequestor(this, APIKey.KEY, this);
        requestMoviesWithSortIdentifier(SortIdentifier.TOP_RATED);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(View.SCROLL_AXIS_VERTICAL)) {
                    requestMoviesWithSortIdentifier(SortIdentifier.TOP_RATED);
                }
            }
        });

    }


    private void requestMoviesWithSortIdentifier(SortIdentifier identifier) {
        int currentPage = mMovieRequestor.getCurrentPage();
        if (currentPage >= mMovieRequestor.getMaxPages()) return;

        mLoadingIndicator.setVisibility(View.VISIBLE);

        mMovieRequestor.performMovieRequest(identifier,currentPage + 1);
    }


    //region MovieConsumer implementation
    @Override
    public void consumeMovies(List<Movie> movies) {

        if (mMovieRequestor.getCurrentPage() == 1) {
            mAdapter.setMovies(movies);
        } else {
            mAdapter.appendMovies(movies);
        }

        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError(VolleyError error) {
        error.printStackTrace();
    }
    //endregion


}

