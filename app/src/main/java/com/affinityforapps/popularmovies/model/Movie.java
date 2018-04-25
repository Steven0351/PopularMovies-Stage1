package com.affinityforapps.popularmovies.model;

import java.util.Date;

public final class Movie {
    private int id;
    private String title;
    private String synopsis;
    private Date releaseDate;
    private double voteAverage;
    private String relativeImageUrl;

    public Movie(int id, String title, String synopsis, Date releaseDate, double voteAverage,
                 String relativeImageUrl) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.relativeImageUrl = relativeImageUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getImageUrl() {
        return "http://image.tmdb.org/t/p/w185" + relativeImageUrl;
    }
}
