package com.affinityforapps.popularmovies.utils;

import com.affinityforapps.popularmovies.model.Movie;
import com.android.volley.VolleyError;

import java.util.List;

public interface MovieConsumer {
    void consumeMovies(List<Movie> movies);
    void onError(VolleyError error);
}
