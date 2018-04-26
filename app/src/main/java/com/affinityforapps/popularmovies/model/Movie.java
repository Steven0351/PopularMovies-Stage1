package com.affinityforapps.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public final class Movie {
    private int id;
    private String title;
    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("poster_path")
    private String posterPath;

    public Movie(int id, String title, String overview, Date releaseDate, double voteAverage,
                 String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getImageUrl() {
        return "http://image.tmdb.org/t/p/w185" + posterPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", voteAverage=" + voteAverage +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
