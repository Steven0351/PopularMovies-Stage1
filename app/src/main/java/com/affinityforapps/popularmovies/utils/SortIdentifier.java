package com.affinityforapps.popularmovies.utils;

public enum SortIdentifier {
    POPULAR ("popular"),
    TOP_RATED ("top_rated");

    private final String identifier;

    SortIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
