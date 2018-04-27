package com.affinityforapps.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.affinityforapps.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";

    private Movie mMovie;


    public static Intent newIntent(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mMovie.getTitle());
    }

    private void setupUI() {
        ImageView mPosterImageView = findViewById(R.id.iv_movie_detail_poster);
        TextView mReleaseDateTextView = findViewById(R.id.tv_release_date);
        TextView mRatingTextView = findViewById(R.id.tv_rating);
        TextView mOverviewTextView = findViewById(R.id.tv_overview);

        Picasso.get()
                .load(mMovie.getImageUrl())
                .into(mPosterImageView);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMovie.getReleaseDate());
        mReleaseDateTextView.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        DecimalFormat df = new DecimalFormat("#.#");
        String ratingAverage = String.valueOf(df.format(mMovie.getVoteAverage()));
        mRatingTextView.setText(getString(R.string.movie_average_vote, ratingAverage));

        mOverviewTextView.setText(mMovie.getOverview());
    }
}
