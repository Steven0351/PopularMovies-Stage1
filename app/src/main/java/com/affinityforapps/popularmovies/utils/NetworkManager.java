package com.affinityforapps.popularmovies.utils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

final class NetworkManager {

    private NetworkResponseHandler networkResponseHandler;

    NetworkManager(NetworkResponseHandler networkResponseHandler) {
        this.networkResponseHandler = networkResponseHandler;
    }

    StringRequest fetchRequest(int requestMethod, String url) {
        return new StringRequest(requestMethod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                networkResponseHandler.handleJsonResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkResponseHandler.handleNetworkError(error);
            }
        });
    }

}
