package com.affinityforapps.popularmovies.utils;

import com.android.volley.VolleyError;

interface NetworkResponseHandler {
    void handleJsonResponse(String jsonString);
    void handleNetworkError(VolleyError error);
}
