package com.affinityforapps.popularmovies.utils;

import android.content.Context;
import android.net.Uri;

import com.affinityforapps.popularmovies.model.Movie;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieRequestor implements NetworkResponseHandler {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String KEY_QUERY_PARAM = "api_key";
    private static final String PAGE_QUERY_PARAM = "page";


    private NetworkManager mNetworkManager;
    private MovieConsumer mMovieConsumer;
    private String mApiKey;
    private Context mContext;
    private int mCurrentPage;
    private int mMaxPages;

    public MovieRequestor(Context context, String apiKey, MovieConsumer movieConsumer) {
        mContext = context;
        mNetworkManager = new NetworkManager(this);
        mApiKey = apiKey;
        mMovieConsumer = movieConsumer;
        mCurrentPage = 0;
        mMaxPages = 1;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setMaxPages(int maxPages) {
        mMaxPages = maxPages;
    }

    public int getMaxPages() {
        return mMaxPages;
    }

    public void performMovieRequest(SortIdentifier sortIdentifier, int pageNumber) {
        Uri uri = createUriFromSortIdentifier(sortIdentifier, pageNumber);
        if (uri == null) {
            return;
        }

        StringRequest movieRequest = mNetworkManager
                .fetchRequest(Request.Method.GET, uri.toString());

        Volley.newRequestQueue(mContext)
                .add(movieRequest);
    }

    private Uri createUriFromSortIdentifier(SortIdentifier sortIdentifier, int pageNumber) {
        if (pageNumber > mMaxPages) {
            return null;
        }

        return Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortIdentifier.getIdentifier())
                .appendQueryParameter(KEY_QUERY_PARAM, mApiKey)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(pageNumber))
                .build();
    }

    @Override
    public void handleJsonResponse(String jsonString) {
        Gson gson = new Gson();

        MovieResponse movieResponse = gson.fromJson(jsonString, MovieResponse.class);
        mCurrentPage = movieResponse.getCurrentPage();
        mMaxPages = movieResponse.getTotalPages();
        mMovieConsumer.consumeMovies(movieResponse.getMovies());
    }

    @Override
    public void handleNetworkError(VolleyError error) {
        mMovieConsumer.onError(error);
    }

    private class MovieResponse {
        @SerializedName("results")
        private List<Movie> mMovies;
        @SerializedName("page")
        int mCurrentPage;
        @SerializedName("total_pages")
        int mTotalPages;

        MovieResponse(List<Movie> movies, int currentPage, int totalPages) {
            mMovies = movies;
            mCurrentPage = currentPage;
            mTotalPages = totalPages;
        }

        List<Movie> getMovies() {
            return mMovies;
        }

        int getCurrentPage() {
            return mCurrentPage;
        }

        int getTotalPages() {
            return mTotalPages;
        }
    }
}
