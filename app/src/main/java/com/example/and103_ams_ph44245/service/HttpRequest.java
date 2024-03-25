package com.example.and103_ams_ph44245.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private APIService apiService;

    public HttpRequest() {
        apiService = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APIService.class);
    }

    public APIService callAPI() {
        return apiService;
    }
}
