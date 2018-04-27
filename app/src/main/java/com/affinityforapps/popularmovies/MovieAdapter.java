package com.affinityforapps.popularmovies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.affinityforapps.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> mMovies;

    /**
     * Call when replacing the current list of movies is required
     * @param movies
     */
    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    /**
     * Call when appending the current list of movies is required
     * @param additionalMovies
     */
    public void appendMovies(List<Movie> additionalMovies) {
        mMovies.addAll(additionalMovies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    //region RecyclerView.Adapter implementation
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_movie, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        holder.bindMovie(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        }

        return mMovies.size();
    }
    //endregion

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mPosterImageView;
        private TextView mMovieTitleTextView;
        private Movie mMovie;

        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mPosterImageView = itemView.findViewById(R.id.iv_movie);
            mMovieTitleTextView = itemView.findViewById(R.id.tv_movie_title);
        }

        void bindMovie(Movie movie) {
            mMovie = movie;
            mMovieTitleTextView.setText(movie.getTitle());
            Picasso.get()
                    .load(movie.getImageUrl())
                    .into(mPosterImageView);
        }

        @Override
        public void onClick(View view) {
            Log.i("onClick", "entered");
            Intent intent = MovieDetailActivity.newIntent(view.getContext(), mMovie);
            view.getContext().startActivity(intent);
            Log.i("onClick", "exited");
        }
    }
}
